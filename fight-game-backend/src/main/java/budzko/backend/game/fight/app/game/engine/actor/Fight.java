package budzko.backend.game.fight.app.game.engine.actor;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class Fight {

    /**
     * Will be used to send to client side
     */
    private final String fightId = UUID.randomUUID().toString();
    private final long createdAt = System.currentTimeMillis();
    private final Map<String, Player> players = new ConcurrentHashMap<>();

    @Setter
    private boolean isChanged;

    public static Fight createFight() {
        return new Fight();
    }
}
