package budzko.backend.game.fight.app.game.engine;

import budzko.backend.game.fight.app.game.engine.actor.Fight;
import budzko.backend.game.fight.app.game.engine.actor.Player;
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
import java.util.Objects;
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
        if (Objects.requireNonNull(playerEvent.getPlayerEventType()) == PlayerEventType.ARROW) {
            Optional.ofNullable(fights.get(fightId))
                    .ifPresentOrElse(
                            fight -> Optional.ofNullable(fight.getPlayers().get(userId))
                                    .ifPresentOrElse(
                                            player -> player.setPlayerMoveState(playerEvent.getPlayerMoveState()),
                                            () -> {
                                                throw throwPlayerNotFound(fightId, userId);
                                            }
                                    ),
                            () -> {
                                throw throwFightNotFound(fightId);
                            }
                    );
        }
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
                boolean changed = false;
                int stepPixels = uiConfig.getAction().getStepPixels();
                int canvasWidth = uiConfig.getCanvas().getWidth();
                int canvasHeight = uiConfig.getCanvas().getHeight();

                PlayerMoveState playerMoveState = player.getPlayerMoveState();
                if (playerMoveState.isUp()) {
                    if (player.getY() > player.getRadius() + stepPixels) {
                        player.setY(player.getY() - stepPixels);
                    } else {
                        player.setY(player.getRadius());
                    }
                    changed = true;
                }
                if (playerMoveState.isDown()) {
                    if (player.getY() < canvasHeight - stepPixels - player.getRadius()) {
                        player.setY(player.getY() + stepPixels);
                    } else {
                        player.setY(canvasHeight - player.getRadius());
                    }
                    changed = true;
                }
                if (playerMoveState.isLeft()) {
                    if (player.getX() > player.getRadius() + stepPixels) {
                        player.setX(player.getX() - stepPixels);
                    } else {
                        player.setX(player.getRadius());
                    }
                    changed = true;
                }
                if (playerMoveState.isRight()) {
                    if (player.getX() < canvasWidth - stepPixels - player.getRadius()) {
                        player.setX(player.getX() + stepPixels);
                    } else {
                        player.setX(canvasWidth - player.getRadius());
                    }
                    changed = true;
                }
                return changed;
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
