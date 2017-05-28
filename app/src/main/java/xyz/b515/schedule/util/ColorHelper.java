package xyz.b515.schedule.util;

import android.graphics.Color;
import android.support.annotation.ColorInt;

/**
 * Created by Yun on 2017.5.28.
 */

public class ColorHelper {

    public static int shadeColor(@ColorInt int color, double percent) {
        String hex = String.format("#%06X", (0xFFFFFF & color));
        long f = Long.parseLong(hex.substring(1), 16);
        double t = percent < 0 ? 0 : 255;
        double p = percent < 0 ? percent * -1 : percent;
        long R = f >> 16;
        long G = f >> 8 & 0x00FF;
        long B = f & 0x0000FF;
        int alpha = Color.alpha(color);
        int red = (int) (Math.round((t - R) * p) + R);
        int green = (int) (Math.round((t - G) * p) + G);
        int blue = (int) (Math.round((t - B) * p) + B);
        return Color.argb(alpha, red, green, blue);
    }
}
