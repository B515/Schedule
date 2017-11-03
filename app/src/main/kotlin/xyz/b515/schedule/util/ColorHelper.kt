package xyz.b515.schedule.util

import android.graphics.Color
import android.support.annotation.ColorInt

/**
 * Created by Yun on 2017.5.28.
 */

object ColorHelper {
    fun shadeColor(@ColorInt color: Int, percent: Double): Int {
        val hex = String.format("#%06X", 0xFFFFFF and color)
        val f = java.lang.Long.parseLong(hex.substring(1), 16)
        val t = (if (percent < 0) 0 else 255).toDouble()
        val p = if (percent < 0) percent * -1 else percent
        val R = f shr 16
        val G = f shr 8 and 0x00FF
        val B = f and 0x0000FF
        val alpha = Color.alpha(color)
        val red = (Math.round((t - R) * p) + R).toInt()
        val green = (Math.round((t - G) * p) + G).toInt()
        val blue = (Math.round((t - B) * p) + B).toInt()
        return Color.argb(alpha, red, green, blue)
    }
}
