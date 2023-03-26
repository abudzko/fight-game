package budzko.backend.game.fight.app.messaging.message.response;

import budzko.backend.game.fight.app.messaging.message.Message;
import budzko.backend.game.fight.app.messaging.message.MessageType;
import lombok.Data;

@Data
public class FightCreatedResponseMessage extends Message {
    private String playerPublicId;
    private String fightId;

    public static FightCreatedResponseMessage create() {
        FightCreatedResponseMessage fightCreatedResponseMessage = new FightCreatedResponseMessage();
        fightCreatedResponseMessage.setMsgType(MessageType.FIGHT_CREATED);
        return fightCreatedResponseMessage;
    }
}
