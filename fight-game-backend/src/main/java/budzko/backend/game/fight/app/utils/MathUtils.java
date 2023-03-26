package budzko.backend.game.fight.app.utils;

import lombok.experimental.UtilityClass;

import java.util.Random;

@UtilityClass
public class MathUtils {
    private static final Random RANDOM = new Random();

    public static Random random() {
        return RANDOM;
    }
}
