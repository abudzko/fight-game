package budzko.backend.game.fight.app.game.manager.player.event;

import budzko.backend.game.fight.app.game.engine.actor.PlayerMouseState;
import budzko.backend.game.fight.app.game.engine.actor.PlayerMoveState;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlayerEvent {
    private PlayerEventType playerEventType;
    private String userId;
    private PlayerMoveState playerMoveState;
    private PlayerMouseState playerMouseState;
}
