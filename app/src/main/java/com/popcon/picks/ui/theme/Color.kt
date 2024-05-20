package com.popcon.picks.ui.theme

import androidx.compose.ui.graphics.Color
sealed class ThemeColors(
    val background :Color,
    val surface : Color,
    val primary : Color,
    val text : Color){
     data object Dark : ThemeColors(
        background = Color.Black,
        surface = Color.DarkGray,
        primary = Color.White,
        text = Color.White
    )
     data object Light : ThemeColors(
        background = Color.White,
        surface = Color.LightGray,
        primary = Color.Black,
        text = Color.Black
    )
}