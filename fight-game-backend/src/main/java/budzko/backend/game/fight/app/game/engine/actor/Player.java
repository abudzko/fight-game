package budzko.backend.game.fight.app.game.engine.actor;

import lombok.Data;

import java.util.UUID;

@Data
public class Player {
    private final String playerPublicId = UUID.randomUUID().toString();
    private PlayerMoveState playerMoveState = PlayerMoveState.builder().build();
    private PlayerMouseState playerMouseState = PlayerMouseState.builder().build();
    private String userId;
    private double x;
    private double y;
    private int radius;
    private String color;
}
