package budzko.backend.game.fight.app.game.engine.actor;

import lombok.Data;

import java.util.UUID;

@Data
public class Player {
    private final String playerPublicId = UUID.randomUUID().toString();
    private PlayerMoveState playerMoveState = new PlayerMoveState();
    private String userId;
    private int x;
    private int y;
    private int radius;
    private String color;
}
