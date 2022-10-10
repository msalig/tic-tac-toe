package net.salig.tictactoe.game

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.salig.tictactoe.R

data class ButtonUiState(
    @DrawableRes var drawable: Int = R.drawable.ic_nothing,
    var tint: Color = Color.Black,
    @StringRes var contentDescription: Int = R.string.nothing,
    var enabled: Boolean = true
)

class GameScreenViewModel : ViewModel() {
    var turn = mutableStateOf(1)

    val gridSize = 3

    var highScorePlayerOne = 0
    var highScorePlayerTwo = 0

    private var list = Array(gridSize * gridSize) { 0 }

    //For Animation of Won line
    var gameBoardSizeDp: Int = 0
    var gameBoardSizePx: Float = 0f
    var lineCoordinates: List<Float> = listOf(0f, 0f, 0f, 0f)
    private var checkIfWonList = Array(2 * gridSize + 2) { mutableListOf<Int>() }
    var wonDirection = "0"


    var showDialog = mutableStateOf(false)
    var winner = mutableStateOf(R.string.nothing)

    var buttonUiStates = mutableStateListOf<ButtonUiState>()

    init {
        viewModelScope.launch {
            for (i in 0 until gridSize * gridSize) buttonUiStates.add(i, ButtonUiState())
        }

        for (i in 0 until 2 * gridSize + 2)
            if (i < gridSize)
                for (colItem in i * gridSize until (i + 1) * gridSize)
                    checkIfWonList[i].add(colItem)
            else if (i >= gridSize && i < 2 * gridSize)
                for (rowItem in i - gridSize..(gridSize - 1) * gridSize + (i - gridSize) step gridSize)
                    checkIfWonList[i].add(rowItem)
            else if (i == 2 * gridSize)
                for (item in 0 until gridSize * gridSize step gridSize + 1)
                    checkIfWonList[i].add(item)
            else if (i == 2 * gridSize + 1)
                for (item in gridSize - 1..gridSize * gridSize - gridSize step gridSize - 1)
                    checkIfWonList[i].add(item)

    }

    fun resetButtonUiStates() {
        buttonUiStates.clear()
        viewModelScope.launch {
            for (i in 0 until gridSize * gridSize) buttonUiStates.add(i, ButtonUiState())
        }

        list = Array(gridSize * gridSize) { 0 }
        lineCoordinates = listOf(0f, 0f, 0f, 0f)

    }

    fun updateGameBoardButton(itemIndex: Int, button: ButtonUiState) {
        buttonUiStates[itemIndex] = button
        list[itemIndex] = turn.value

        lineCoordinates = checkIfWon2()
        //checkIfWon()
    }

    /*
        private fun checkIfWon() {
            if (list[0] == turn.value && list[1] == turn.value && list[2] == turn.value || list[3] == turn.value && list[4] == turn.value && list[5] == turn.value ||
                list[6] == turn.value && list[7] == turn.value && list[8] == turn.value || list[0] == turn.value && list[3] == turn.value && list[6] == turn.value ||
                list[1] == turn.value && list[4] == turn.value && list[7] == turn.value || list[2] == turn.value && list[5] == turn.value && list[8] == turn.value ||
                list[0] == turn.value && list[4] == turn.value && list[8] == turn.value || list[2] == turn.value && list[4] == turn.value && list[6] == turn.value
            ) {
                showDialog.value = true
                winner.value = if (turn.value == 1) R.string.player_one else R.string.player_two
                if (turn.value == 1) ++highScorePlayerOne else if (turn.value == 2) ++highScorePlayerTwo
            } else if (!list.contains(0)) {
                showDialog.value = true
                winner.value = R.string.draw
            }

            turn.value = if (turn.value == 1) 2 else 1
        }
    */
    private fun checkIfWon2(): List<Float> {
        for ((rowIndex, row) in checkIfWonList.withIndex())
            for ((index, item) in row.withIndex()) {
                if (list[item] != turn.value)
                    break
                if (index == gridSize - 1) {
                    showDialog.value = true
                    winner.value = if (turn.value == 1) R.string.player_one else R.string.player_two
                    if (turn.value == 1) ++highScorePlayerOne else ++highScorePlayerTwo
                    if (rowIndex < gridSize) {
                        wonDirection = "|"
                        return listOf(
                            (gameBoardSizePx / (2 * gridSize) + rowIndex * (gameBoardSizePx / gridSize)),
                            0f,
                            (gameBoardSizePx / (2 * gridSize) + rowIndex * (gameBoardSizePx / gridSize)),
                            gameBoardSizePx
                        )
                    } else if (rowIndex >= gridSize && rowIndex < 2 * gridSize) {
                        wonDirection = "-"
                        return listOf(
                            0f,
                            (gameBoardSizePx / (2 * gridSize) + (rowIndex - gridSize) * (gameBoardSizePx / gridSize)),
                            gameBoardSizePx,
                            (gameBoardSizePx / (2 * gridSize) + (rowIndex - gridSize) * (gameBoardSizePx / gridSize))
                        )
                    } else if (rowIndex == 2 * gridSize && gridSize % 2 != 0) {
                        wonDirection = "\\"
                        return listOf(0f, 0f, gameBoardSizePx, gameBoardSizePx)
                    } else if (rowIndex == 2 * gridSize + 1 && gridSize % 2 != 0) {
                        wonDirection = "/"
                        return listOf(gameBoardSizePx, 0f, 0f, gameBoardSizePx)
                    }
                }
            }

        if (!list.contains(0)) {
            showDialog.value = true
            winner.value = R.string.draw
        }

        turn.value = if (turn.value == 1) 2 else 1

        return listOf(0f, 0f, 0f, 0f)
    }
}
/*
    private fun checkIfWon3(): List<Float> {
        for (column in 0..(gridSize - 1) * gridSize step gridSize) {
            for (colItem in 0 until gridSize) {
                if (list[column + colItem] != turn)
                    break
                if (colItem == gridSize - 1) {
                    showDialog.value = true
                    winner.value = if (turn == 1) R.string.player_one else R.string.player_two
                    return listOf(
                        (gameBoardSizePx / (2 * gridSize) + column * (gameBoardSizePx / gridSize) / gridSize),
                        0f,
                        (gameBoardSizePx / (2 * gridSize) + column * (gameBoardSizePx / gridSize) / gridSize),
                        gameBoardSizePx
                    )
                }
            }
        }
        for (row in 0 until gridSize) {
            for (rowItem in 0..(gridSize - 1) * gridSize step gridSize) {
                if (list[row + rowItem] != turn)
                    break
                if (rowItem == (gridSize - 1) * gridSize) {
                    showDialog.value = true
                    winner.value = if (turn == 1) R.string.player_one else R.string.player_two
                    return listOf(
                        0f,
                        (gameBoardSizePx / (2 * gridSize) + row * (gameBoardSizePx / gridSize)),
                        gameBoardSizePx,
                        (gameBoardSizePx / (2 * gridSize) + row * (gameBoardSizePx / gridSize))
                    )
                }
            }
        }
        if (gridSize % 2 != 0) {
            for (item in 0 until gridSize * gridSize step gridSize + 1) {
                if (list[item] != turn)
                    break
                if (item == gridSize * gridSize - 1) {
                    showDialog.value = true
                    winner.value = if (turn == 1) R.string.player_one else R.string.player_two
                    return listOf(0f, 0f, gameBoardSizePx, gameBoardSizePx)
                }
            }
            for (item in gridSize - 1..gridSize * gridSize - gridSize step gridSize - 1) {
                if (list[item] != turn)
                    break
                if (item == gridSize * gridSize - gridSize) {
                    showDialog.value = true
                    winner.value = if (turn == 1) R.string.player_one else R.string.player_two
                    return listOf(gameBoardSizePx, 0f, 0f, gameBoardSizePx)
                }
            }
        }

        turn = if (turn == 1) 2 else 1

        return listOf(0f, 0f, 0f, 0f)
    }

 */
