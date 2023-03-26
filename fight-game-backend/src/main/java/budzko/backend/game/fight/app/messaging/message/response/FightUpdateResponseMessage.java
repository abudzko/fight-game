package budzko.backend.game.fight.app.messaging.message.response;

import budzko.backend.game.fight.app.messaging.message.Message;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FightUpdateResponseMessage extends Message {
    private List<PlayerState> states = new ArrayList<>();

    public static FightUpdateResponseMessage create() {
        return new FightUpdateResponseMessage();
    }
}
