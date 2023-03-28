package budzko.backend.game.fight.app.game.manager;

import budzko.backend.game.fight.app.game.engine.GameEngine;
import budzko.backend.game.fight.app.game.manager.player.PlayerContext;
import budzko.backend.game.fight.app.game.manager.player.event.PlayerEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class FightManager {

    protected static final String FAILED_FIGHT_NOT_FOUND_BY_USER_ID = "Failed: fight wasn't found by user id %s";
    private final Map<String, PlayerContext> players = new ConcurrentHashMap<>();

    private final GameEngine gameEngine;

    public String createFight() {
        return gameEngine.createFight();
    }

    /**
     * Creates new player and adds him into recently created fight or join into existed one
     *
     * @param userId  use websocket session id or generate random. For internal usage, don't share with other players
     * @param fightId id of existed fight
     * @throws IllegalArgumentException fight not found
     */
    public String addPlayer(String userId, String fightId) {
        String playerPublicId = gameEngine.addPlayer(userId, fightId);
        players.computeIfAbsent(
                userId,
                id -> PlayerContext.builder()
                        .userId(id)
                        .fightId(fightId)
                        .playerPublicId(playerPublicId)
                        .build()
        );
        return playerPublicId;
    }

    public void updatePlayer(PlayerEvent playerEvent) {
        String userId = playerEvent.getUserId();
        Optional.ofNullable(players.get(userId))
                .ifPresentOrElse(
                        playerContext -> gameEngine.updatePlayer(playerContext.getFightId(), playerEvent),
                        () -> log.warn(FAILED_FIGHT_NOT_FOUND_BY_USER_ID.formatted(userId))
                );

    }

    public void deletePlayer(String userId) {
        Optional.ofNullable(players.get(userId))
                .ifPresentOrElse(
                        playerContext -> gameEngine.deletePlayer(userId, playerContext.getFightId()),
                        () -> log.warn(FAILED_FIGHT_NOT_FOUND_BY_USER_ID.formatted(userId))
                );
    }

    public List<String> getFightIds() {
        //TODO
        return List.of();
    }

    public Optional<String> getRandFightId() {
        return gameEngine.getRandFightId();
    }
}
