package net.salig.tictactoe.presentation.game

import net.salig.tictactoe.core.InitialGridSize
import net.salig.tictactoe.domain.colContainsOnly
import net.salig.tictactoe.domain.containsOnly
import net.salig.tictactoe.domain.deepContainsOnly
import net.salig.tictactoe.domain.diagonallyContainsOnly

fun checkIfSomeoneWon(
    uiStates: Array<Array<GameBoardButtonUIState>>,
    arrPos: ItemPositionInArray,
    setWon: (Boolean) -> Unit,
) {
    //Horizontal
    if ((arrPos.item + 1 < InitialGridSize - 1 && uiStates[arrPos.row][arrPos.item] == uiStates[arrPos.row][arrPos.item + 1] ||
                arrPos.item - 1 >= 0 && uiStates[arrPos.row][arrPos.item] == uiStates[arrPos.row][arrPos.item - 1]) &&
        (uiStates[arrPos.row].containsOnly(uiStates[arrPos.row][arrPos.item]))
    ) setWon(true)
    //Vertical
    else if ((arrPos.row + 1 < InitialGridSize - 1 && uiStates[arrPos.row][arrPos.item] == uiStates[arrPos.row + 1][arrPos.item] ||
                arrPos.row - 1 >= 0 && uiStates[arrPos.row][arrPos.item] == uiStates[arrPos.row - 1][arrPos.item]) &&
        (uiStates.colContainsOnly(arrPos.item, uiStates[arrPos.row][arrPos.item]))
    ) setWon(true)
    //Over Cross
    else if ((arrPos.row == arrPos.item || arrPos.row + arrPos.item == InitialGridSize - 1) &&
        (uiStates.diagonallyContainsOnly(uiStates[arrPos.row][arrPos.item]))
    ) setWon(true)
    //Draw
    else if (!uiStates.deepContainsOnly(GameBoardButtonUIState.NOTHING)) setWon(false)
}