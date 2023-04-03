package net.salig.tictactoe.data.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import net.salig.tictactoe.R
import net.salig.tictactoe.core.theme.CircleBlue
import net.salig.tictactoe.core.theme.CrossRed

enum class GameBoardButtonUIStates(
    @DrawableRes val drawable: Int,
    val tint: Color,
    @StringRes val contentDescription: Int,
) {
    CROSS(R.drawable.ic_cross, CrossRed, R.string.cross),
    CIRCLE(R.drawable.ic_circle, CircleBlue, R.string.circle),
    NOTHING(R.drawable.ic_nothing, Color.Black, R.string.nothing);
}