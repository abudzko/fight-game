package budzko.backend.game.fight.app.messaging.message;

import budzko.backend.game.fight.app.game.engine.actor.Fight;
import budzko.backend.game.fight.app.messaging.message.response.FightCreatedResponseMessage;
import budzko.backend.game.fight.app.messaging.message.response.FightUpdateResponseMessage;
import budzko.backend.game.fight.app.messaging.message.response.JoinedToFightResponseMessage;
import budzko.backend.game.fight.app.messaging.message.response.PlayerState;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageFactory {

    /**
     * Response on {@link MessageType#CREATE_FIGHT} message
     */
    public FightCreatedResponseMessage buildFightCreatedResponseMessage(
            String userId,
            String playerPublicId,
            String fightId
    ) {
        FightCreatedResponseMessage message = FightCreatedResponseMessage.create();
        message.setUserId(userId);
        message.setPlayerPublicId(playerPublicId);
        message.setFightId(fightId);
        return message;
    }

    public Message buildFightUpdateMessage(Fight fight) {
        FightUpdateResponseMessage message = FightUpdateResponseMessage.create();
        message.setMsgType(MessageType.FIGHT_UPDATE);
        List<PlayerState> states = new ArrayList<>();
        fight.getPlayers().forEach((id, player) -> {
            PlayerState playerState = PlayerState.builder()
                    .x(player.getX())
                    .y(player.getY())
                    .color(player.getColor())
                    .radius(player.getRadius())
                    .playerId(player.getPlayerPublicId())
                    .build();
            states.add(playerState);

        });
        message.setStates(states);
        return message;
    }

    public JoinedToFightResponseMessage buildJoinedToFightMessage(
            String userId,
            String playerPublicId,
            String fightId
    ) {
        JoinedToFightResponseMessage message = JoinedToFightResponseMessage.create();
        message.setUserId(userId);
        message.setPlayerPublicId(playerPublicId);
        message.setFightId(fightId);
        return message;
    }
}
