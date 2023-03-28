package budzko.backend.game.fight.app.game.engine;

import budzko.backend.game.fight.app.game.engine.actor.Fight;
import budzko.backend.game.fight.app.game.engine.actor.Player;
import budzko.backend.game.fight.app.game.engine.actor.PlayerMouseState;
import budzko.backend.game.fight.app.game.engine.actor.PlayerMoveState;
import budzko.backend.game.fight.app.game.manager.player.event.PlayerEvent;
import budzko.backend.game.fight.app.game.manager.player.event.PlayerEventType;
import budzko.backend.game.fight.app.messaging.MessageSender;
import budzko.backend.game.fight.app.messaging.message.Message;
import budzko.backend.game.fight.app.messaging.message.MessageFactory;
import budzko.backend.game.fight.app.utils.DateUtils;
import budzko.backend.game.fight.app.utils.MathUtils;
import budzko.backend.game.fight.app.utils.PlayerUtils;
import budzko.backend.game.fight.app.web.controller.config.ui.UIConfig;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class GameEngine {
    private final Map<String, Fight> fights = new ConcurrentHashMap<>();
    private final MessageSender messageSender;
    private final MessageFactory messageFactory;
    @Autowired
    private final UIConfig uiConfig;
    @Value("${app.fight.inactiveTimeoutSec}")
    private int fightInactiveTimeoutSec;

    private static RuntimeException throwFightNotFound(String fightId) {
        return new IllegalArgumentException("Fight %s not found".formatted(fightId));
    }

    private static RuntimeException throwPlayerNotFound(String fightId, String userId) {
        return new IllegalArgumentException("Player in fight %s with user id %s not found".formatted(fightId, userId));
    }

    private boolean isAbandoned(Fight fight) {
        return fight.getPlayers().size() == 0
                && DateUtils.now() > fight.getCreatedAt() + TimeUnit.SECONDS.toMillis(fightInactiveTimeoutSec);
    }

    @PostConstruct
    private void init() {
        runActionLoopTask();
    }

    public String addPlayer(String userId, String fightId) {
        Fight fight = fights.get(fightId);
        if (fight == null) {
            throw throwFightNotFound(fightId);
        } else {
            return fight.getPlayers()
                    .computeIfAbsent(
                            userId, uId -> {
                                Player player = PlayerUtils.createPlayer(uId);
                                fight.setChanged(true);
                                return player;
                            })
                    .getPlayerPublicId();
        }
    }

    public String createFight() {
        Fight fight = Fight.createFight();
        fights.put(fight.getFightId(), fight);
        synchronized (fights) {
            if (fights.size() > 0) {
                fights.notifyAll();
            }
        }
        return fight.getFightId();
    }

    public void deletePlayer(String userId, String fightId) {
        Fight fight = fights.get(fightId);
        if (fight == null) {
            log.warn("Failed: fight %s wasn't found by user id %s".formatted(userId, fightId));
        } else {
            fight.getPlayers().remove(userId);
            fight.setChanged(true);
            log.warn("Player by user id %s was deleted from fight %s".formatted(userId, fightId));
        }
    }

    public void updatePlayer(String fightId, PlayerEvent playerEvent) {
        String userId = playerEvent.getUserId();
        Optional.ofNullable(fights.get(fightId))
                .ifPresentOrElse(
                        fight -> Optional.ofNullable(fight.getPlayers().get(userId))
                                .ifPresentOrElse(
                                        player -> {

                                            PlayerEventType playerEventType = playerEvent.getPlayerEventType();
                                            switch (playerEventType) {
                                                case ARROW ->
                                                        player.setPlayerMoveState(playerEvent.getPlayerMoveState());
                                                case MOUSE ->
                                                        player.setPlayerMouseState(playerEvent.getPlayerMouseState());
                                                default -> throw new IllegalStateException(
                                                        "Failed: Not supported event type %s"
                                                                .formatted(playerEventType)
                                                );
                                            }
                                        },
                                        () -> {
                                            throw throwPlayerNotFound(fightId, userId);
                                        }
                                ),
                        () -> {
                            throw throwFightNotFound(fightId);
                        }
                );
    }

    private void runActionLoopTask() {
        Thread task = new Thread(new Runnable() {
            @Override
            public void run() {
                log.info("runActionLoopTask started");
                try {
                    while (true) {
                        waitFights();
                        Set<String> fightIdsCopy = new HashSet<>(fights.keySet());
                        for (String fightId : fightIdsCopy) {
                            Fight fight = fights.get(fightId);
                            if (fight != null) {
                                if (isAbandoned(fight)) {
                                    fights.remove(fightId);
                                    log.info("Fight was removed %s".formatted(fightId));
                                    continue;
                                }
                                boolean changed = false;
                                Map<String, Player> players = fight.getPlayers();
                                for (Player player : players.values()) {
                                    if (changePosition(player)) {
                                        changed = true;
                                    }
                                }

                                if (changed || fight.isChanged()) {
                                    sendUpdates(fight);
                                    fight.setChanged(false);
                                }
                            }
                        }
                        pause(uiConfig.getAction().getActionIntervalMs());
                    }
                } catch (RuntimeException e) {
                    log.error(e.getMessage(), e);
                } finally {
                    log.info("runActionLoopTask finished");
                }
            }

            private void pause(int duration) {
                try {
                    Thread.sleep(duration);
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                }
            }

            private void sendUpdates(Fight fight) {
                Message message = messageFactory.buildFightUpdateMessage(fight);
                fight.getPlayers().forEach((id, player) -> {
                    message.setUserId(id);
                    messageSender.send(message);
                });
            }

            private void waitFights() {
                if (fights.size() == 0) {
                    synchronized (fights) {
                        if (fights.size() == 0) {
                            try {
                                log.info("runActionLoopTask wait");
                                fights.wait();
                                log.info("runActionLoopTask continue");
                            } catch (InterruptedException e) {
                                log.error(e.getMessage(), e);
                            }
                        }
                    }
                }
            }

            boolean changePosition(Player player) {
                boolean changed;
                changed = handleMoveState(player);
                if (changed) {
                    player.getPlayerMouseState().reset();
                } else {
                    changed = handleMouseState(player);
                }
                return changed;
            }

            private boolean handleMouseState(Player player) {
                boolean changed = false;
                PlayerMouseState playerMouseState = player.getPlayerMouseState();
                double playerX = player.getX();
                double playerY = player.getY();
                if (Math.abs(playerX - playerMouseState.getX()) <= Math.abs(playerMouseState.getDX())
                        && Math.abs(playerY - playerMouseState.getY()) <= Math.abs(playerMouseState.getDY())) {
                    //Arrived at the destination
                    playerMouseState.reset();
                    return false;
                }
                if (playerMouseState.isActive()) {
                    if (!playerMouseState.isCalculated()) {
                        calculate(playerMouseState, player);
                    }
                    boolean up = playerMouseState.getDY() < 0;
                    boolean down = playerMouseState.getDY() > 0;
                    boolean left = playerMouseState.getDX() < 0;
                    boolean right = playerMouseState.getDX() > 0;
                    changed = doStep(
                            player,
                            Math.abs(playerMouseState.getDX()),
                            Math.abs(playerMouseState.getDY()),
                            up,
                            down,
                            left,
                            right
                    );
                }
                return changed;
            }

            private boolean handleMoveState(Player player) {
                PlayerMoveState playerMoveState = player.getPlayerMoveState();
                boolean up = playerMoveState.isUp();
                boolean down = playerMoveState.isDown();
                boolean left = playerMoveState.isLeft();
                boolean right = playerMoveState.isRight();
                int stepPixels = uiConfig.getAction().getStepPixels();
                return doStep(player, stepPixels, stepPixels, up, down, left, right);
            }

            private boolean doStep(
                    Player player,
                    double dx,
                    double dy,
                    boolean up,
                    boolean down,
                    boolean left,
                    boolean right
            ) {
                int canvasWidth = uiConfig.getCanvas().getWidth();
                int canvasHeight = uiConfig.getCanvas().getHeight();
                boolean changed = false;

                if (up) {
                    if (player.getY() > player.getRadius() + dy) {
                        player.setY(player.getY() - dy);
                    } else {
                        player.setY(player.getRadius());
                    }
                    changed = true;
                }
                if (down) {
                    if (player.getY() < canvasHeight - dy - player.getRadius()) {
                        player.setY(player.getY() + dy);
                    } else {
                        player.setY(canvasHeight - player.getRadius());
                    }
                    changed = true;
                }
                if (left) {
                    if (player.getX() > player.getRadius() + dx) {
                        player.setX(player.getX() - dx);
                    } else {
                        player.setX(player.getRadius());
                    }
                    changed = true;
                }
                if (right) {
                    if (player.getX() < canvasWidth - dx - player.getRadius()) {
                        player.setX(player.getX() + dx);
                    } else {
                        player.setX(canvasWidth - player.getRadius());
                    }
                    changed = true;
                }
                return changed;
            }

            private void calculate(PlayerMouseState playerMouseState, Player player) {
                int x = playerMouseState.getX();
                int y = playerMouseState.getY();
                int stepPixels = uiConfig.getAction().getStepPixels();
                double lx = x - player.getX();
                double ly = y - player.getY();
                double distance = Math.sqrt(Math.pow(lx, 2) + Math.pow(ly, 2));
                double cosA = lx / distance;
                double sinA = ly / distance;

                double dx = stepPixels * cosA;
                double dy = stepPixels * sinA;

                playerMouseState.setDX(dx);
                playerMouseState.setDY(dy);
                playerMouseState.setCalculated(true);
            }
        });
        task.start();
    }

    public Optional<String> getRandFightId() {
        if (fights.size() == 0) {
            return Optional.empty();
        }
        List<String> fightIds = new ArrayList<>(fights.keySet());
        int index = MathUtils.random().nextInt(fightIds.size());
        return Optional.of(fightIds.get(index));
    }
}
