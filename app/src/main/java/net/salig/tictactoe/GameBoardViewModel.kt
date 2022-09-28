package net.salig.tictactoe

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class GameBoardViewModel : ViewModel() {
    var turn by mutableStateOf(1)
        private set

    @JvmName("setTurn1")
    fun setTurn(player: Int) {
        turn = player
    }

    var buttonUiStates = mutableStateListOf<ButtonUiState>()

    init {
        viewModelScope.launch {
            for (i in 0..8)
                buttonUiStates.add(i, ButtonUiState())
        }
    }

    fun updateGameBoardButton(itemIndex: Int, button: ButtonUiState) {
        buttonUiStates[itemIndex] = button
    }

}

class ButtonUiState(
    @DrawableRes var drawable: Int = R.drawable.ic_nothing,
    var tint: Color = Color.Black,
    @StringRes var contentDescription: Int = R.string.none,
    var enabled: Boolean = true
) {
    override fun toString(): String {
        return "[$drawable, $tint, $contentDescription, $enabled]"
    }
}