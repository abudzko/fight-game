package budzko.backend.game.fight.app.game.engine.actor;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlayerMoveState {
    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;
}
