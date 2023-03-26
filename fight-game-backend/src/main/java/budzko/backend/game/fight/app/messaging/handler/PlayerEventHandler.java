package budzko.backend.game.fight.app.messaging.handler;

import budzko.backend.game.fight.app.game.engine.actor.PlayerMoveState;
import budzko.backend.game.fight.app.game.manager.FightManager;
import budzko.backend.game.fight.app.game.manager.player.event.PlayerEvent;
import budzko.backend.game.fight.app.game.manager.player.event.PlayerEventType;
import budzko.backend.game.fight.app.messaging.message.Message;
import budzko.backend.game.fight.app.messaging.message.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlayerEventHandler implements Handler {
    private final FightManager fightManager;

    private static PlayerEvent toEvent(Message message) {
        PlayerMoveState playerMoveState = new PlayerMoveState();

        Map<String, Boolean> arrowEvent = message.getArrowEvent();
        playerMoveState.setUp(arrowEvent.get("up"));
        playerMoveState.setDown(arrowEvent.get("down"));
        playerMoveState.setLeft(arrowEvent.get("left"));
        playerMoveState.setRight(arrowEvent.get("right"));

        PlayerEvent playerEvent = new PlayerEvent();
        playerEvent.setPlayerEventType(PlayerEventType.ARROW);
        playerEvent.setUserId(message.getUserId());
        playerEvent.setPlayerMoveState(playerMoveState);
        return playerEvent;
    }

    @Override
    public MessageType getType() {
        return MessageType.PLAYER_STATE;
    }

    @Override
    public void handle(Message message) {
        PlayerEvent playerArrowEvent = toEvent(message);
        fightManager.updatePlayer(playerArrowEvent);
    }
}
