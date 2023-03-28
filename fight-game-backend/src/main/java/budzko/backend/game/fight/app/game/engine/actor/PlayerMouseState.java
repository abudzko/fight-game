package budzko.backend.game.fight.app.game.engine.actor;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlayerMouseState {
    private int x;
    private int y;
    private boolean isActive;
    private boolean calculated;
    private double dX;
    private double dY;

    public void reset() {
        isActive = false;
        calculated = false;
    }
}
