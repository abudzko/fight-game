package budzko.backend.game.fight.app.messaging.message.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlayerState {
    private String playerId;
    private int x;
    private int y;
    private int radius;
    private String color;
}
