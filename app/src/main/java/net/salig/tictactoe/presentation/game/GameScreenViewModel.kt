package net.salig.tictactoe.presentation.game

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.salig.tictactoe.R
import net.salig.tictactoe.core.InitialGridSize

class GameScreenViewModel : ViewModel() {
    var turn = mutableStateOf(1)

    var buttonsAlreadyClicked = 0

    var highScorePlayerOne = 0
    var highScorePlayerTwo = 0

    var showDialog = mutableStateOf(false)
    var winner = mutableStateOf(R.string.nothing)

    val _arrButtonUIStates =
        MutableLiveData(Array(InitialGridSize) { Array(InitialGridSize) { GameBoardButtonUIState.NOTHING } })
    val arrButtonUIStates: LiveData<Array<Array<GameBoardButtonUIState>>> = _arrButtonUIStates

    fun updateGameBoardButton(arrPos: ItemPositionInArray, button: GameBoardButtonUIState) {
        _arrButtonUIStates.value =
            GameBoardButtonUIState.updateArrButtonUIState(_arrButtonUIStates.value!!,
                arrPos,
                button)

        if (buttonsAlreadyClicked >= 4) {
            checkIfSomeoneWon(_arrButtonUIStates.value!!, arrPos, setWon = {
                showDialog.value = true
                when (it) {
                    true -> {
                        winner.value =
                            if (turn.value == 1) R.string.player_one else R.string.player_two
                        if (turn.value == 1) ++highScorePlayerOne else ++highScorePlayerTwo
                    }
                    false -> winner.value = R.string.draw
                }
            })
        } else {
            ++buttonsAlreadyClicked
        }

        turn.value = if (turn.value == 1) 2 else 1
    }
}