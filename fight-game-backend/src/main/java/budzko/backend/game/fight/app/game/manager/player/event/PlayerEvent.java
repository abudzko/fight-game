package budzko.backend.game.fight.app.game.manager.player.event;

import budzko.backend.game.fight.app.game.engine.actor.PlayerMoveState;
import lombok.Data;

@Data
public class PlayerEvent {
    private PlayerEventType playerEventType;
    private String userId;
    private PlayerMoveState playerMoveState;
}
