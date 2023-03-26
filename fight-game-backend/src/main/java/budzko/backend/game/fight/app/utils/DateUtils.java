package budzko.backend.game.fight.app.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DateUtils {
    public static long now() {
        return System.currentTimeMillis();
    }
}
