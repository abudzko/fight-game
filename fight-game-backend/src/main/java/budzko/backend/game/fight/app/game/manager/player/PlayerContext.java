package budzko.backend.game.fight.app.game.manager.player;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlayerContext {
    private String userId;
    private String playerPublicId;
    private String fightId;
}
