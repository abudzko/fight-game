package budzko.backend.game.fight.app.messaging.message.response;

import budzko.backend.game.fight.app.messaging.message.MessageType;
import lombok.Data;

@Data
public class JoinedToFightResponseMessage extends FightCreatedResponseMessage {
    public static JoinedToFightResponseMessage create() {
        JoinedToFightResponseMessage joinedToFightResponseMessage = new JoinedToFightResponseMessage();
        joinedToFightResponseMessage.setMsgType(MessageType.JOINED_TO_FIGHT);
        return joinedToFightResponseMessage;

    }
}
