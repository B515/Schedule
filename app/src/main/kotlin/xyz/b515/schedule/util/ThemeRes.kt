package xyz.b515.schedule.util

import android.support.annotation.ColorRes
import android.support.annotation.StyleRes

import xyz.b515.schedule.R

enum class ThemeRes(val title: Int, @param:StyleRes val style: Int, @param:ColorRes val color: Int) {
    VIOLET_ZERO(R.string.theme_violet_zero, R.style.ThemeVioletZero, R.color.violetZero),
    GREEN_YUN(R.string.theme_green_yun, R.style.ThemeGreenYun, R.color.greenYun),
    BLUE(R.string.theme_blue, R.style.ThemeBlue, R.color.blue),
    RED(R.string.theme_red, R.style.ThemeRed, R.color.red),
    PURPLE(R.string.theme_purple, R.style.ThemePurple, R.color.purple),
    ORANGE(R.string.theme_orange, R.style.ThemeOrange, R.color.orange),
    PINK(R.string.theme_pink, R.style.ThemePink, R.color.pink),
    GREEN(R.string.theme_green, R.style.ThemeGreen, R.color.green),
    TEAL(R.string.theme_teal, R.style.ThemeTeal, R.color.teal),
    INDIGO(R.string.theme_indigo, R.style.ThemeIndigo, R.color.indigo),
    BLACK(R.string.theme_black, R.style.ThemeBlack, R.color.black)
}