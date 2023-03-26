package budzko.backend.game.fight.app.messaging.handler;

import budzko.backend.game.fight.app.messaging.message.Message;
import budzko.backend.game.fight.app.messaging.message.MessageType;

public interface Handler {
    MessageType getType();

    void handle(Message message);
}
