package budzko.backend.game.fight.app.utils;

import budzko.backend.game.fight.app.game.engine.actor.Player;
import lombok.experimental.UtilityClass;

import java.util.Random;

@UtilityClass
public class PlayerUtils {
    private static final Random random = MathUtils.random();

    public static Player createPlayer(String userId) {
        Player player = new Player();
        player.setUserId(userId);
        player.setX(random.nextInt(500));
        player.setY(random.nextInt(500));
        player.setRadius(random.nextInt(50) + 5);
        player.setColor(ColorUtils.getRandomHex());
        return player;
    }
}
