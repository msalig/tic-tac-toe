package net.salig.tictactoe.presentation.game

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import net.salig.tictactoe.R

enum class Player(
    @DrawableRes val drawableRes: Int,
    @StringRes val drawableContentDescription: Int,
    @StringRes val initialPlayerName: Int,
    val playerNumber: Int,
) {
    ONE(R.drawable.ic_cross, R.string.cross, R.string.player_one, 1),
    TWO(R.drawable.ic_circle, R.string.circle, R.string.player_two, 2);
}