package budzko.backend.game.fight.app.messaging.handler;

import budzko.backend.game.fight.app.game.engine.actor.PlayerMouseState;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlayerEventHandler implements Handler {
    private final FightManager fightManager;

    private static PlayerMoveState parseArrowState(Message message) {
        Map<String, Boolean> arrowEvent = message.getArrowEvent();
        return PlayerMoveState.builder()
                .up(arrowEvent.get("up"))
                .down(arrowEvent.get("down"))
                .left(arrowEvent.get("left"))
                .right(arrowEvent.get("right"))
                .build();
    }

    private static PlayerMouseState parseMouseState(Map<String, String> event) {
        int x = Integer.parseInt(event.get("x"));
        int y = Integer.parseInt(event.get("y"));
        return PlayerMouseState.builder().isActive(true).x(x).y(y).build();
    }

    @Override
    public MessageType getType() {
        return MessageType.PLAYER_EVENT;
    }

    @Override
    public void handle(Message message) {
        PlayerEvent.PlayerEventBuilder playerEventBuilder = PlayerEvent.builder()
                .userId(message.getUserId());

        Optional.ofNullable(message.getArrowEvent())
                .ifPresent(event -> playerEventBuilder
                        .playerEventType(PlayerEventType.ARROW)
                        .playerMoveState(parseArrowState(message))
                );
        Optional.ofNullable(message.getMouseEvent())
                .ifPresent(
                        event -> playerEventBuilder
                                //Mouse event override above ones
                                .playerEventType(PlayerEventType.MOUSE)
                                .playerMouseState(parseMouseState(event))
                );
        fightManager.updatePlayer(playerEventBuilder.build());
    }
}
