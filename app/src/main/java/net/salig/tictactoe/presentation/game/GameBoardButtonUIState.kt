package net.salig.tictactoe.presentation.game

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import net.salig.tictactoe.R
import net.salig.tictactoe.core.InitialGridSize
import net.salig.tictactoe.core.theme.CircleBlue
import net.salig.tictactoe.core.theme.CrossRed

enum class GameBoardButtonUIState(
    @DrawableRes val drawable: Int,
    val tint: Color,
    @StringRes val contentDescription: Int,
    val enabled: Boolean,
) {
    CROSS(R.drawable.ic_cross, CrossRed, R.string.cross, false),
    CIRCLE(R.drawable.ic_circle, CircleBlue, R.string.circle, false),
    NOTHING(R.drawable.ic_nothing, Color.Black, R.string.nothing, true);

    companion object {
        fun resetArrButtonUIStates(): Array<Array<GameBoardButtonUIState>> {
            return Array(InitialGridSize) { Array(InitialGridSize) { NOTHING } }
        }

        fun updateArrButtonUIState(
            uiStates: Array<Array<GameBoardButtonUIState>>,
            arrPos: ItemPositionInArray,
            newState: GameBoardButtonUIState,
        ): Array<Array<GameBoardButtonUIState>> {
            val newArr = uiStates.copyOf()
            newArr[arrPos.row][arrPos.item] = newState
            return newArr
        }
    }
}