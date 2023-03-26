package budzko.backend.game.fight.app.utils;

import lombok.experimental.UtilityClass;

import java.awt.Color;
import java.util.Random;

@UtilityClass
public class ColorUtils {
    private static final Random RAND = MathUtils.random();
    private static final int COLOR_BOUND = 256;

    public static Color genRandColor() {
        return new Color(
                getRandomInBounds(0, COLOR_BOUND),
                getRandomInBounds(0, COLOR_BOUND),
                getRandomInBounds(0, COLOR_BOUND)
        );
    }

    private static int getRandomInBounds(int leftBound, int rightBound) {
        return RAND.nextInt(rightBound - leftBound) + leftBound;
    }

    public static String getRandomHex() {
        Color color = genRandColor();
        return String.format(
                "#%02x%02x%02x",
                color.getRed(),
                color.getGreen(),
                color.getBlue()
        );
    }
}
