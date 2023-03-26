package budzko.backend.game.fight.app.game.manager.player.event;

import budzko.backend.game.fight.app.game.engine.actor.PlayerMoveState;
import lombok.Data;

@Data
public class PlayerArrowEvent extends PlayerEvent {
    private PlayerMoveState playerMoveState;
}
