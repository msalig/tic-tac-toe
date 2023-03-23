package net.salig.tictactoe.domain

import net.salig.tictactoe.core.Constants.InitialGridSize
import net.salig.tictactoe.data.serialization.model.Coordinates

fun checkIfSomeoneWon(
    fieldStates: Array<Array<Char?>>,
    arrPos: Coordinates,
    setWon: (Boolean) -> Unit,
) {
    //Horizontal
    if ((arrPos.x + 1 < InitialGridSize - 1 && fieldStates[arrPos.y][arrPos.x] == fieldStates[arrPos.y][arrPos.x + 1] ||
                arrPos.x - 1 >= 0 && fieldStates[arrPos.y][arrPos.x] == fieldStates[arrPos.y][arrPos.x - 1]) &&
        (fieldStates[arrPos.y].containsOnly(fieldStates[arrPos.y][arrPos.x]))
    ) setWon(true)
    //Vertical
    else if ((arrPos.y + 1 < InitialGridSize - 1 && fieldStates[arrPos.y][arrPos.x] == fieldStates[arrPos.y + 1][arrPos.x] ||
                arrPos.y - 1 >= 0 && fieldStates[arrPos.y][arrPos.x] == fieldStates[arrPos.y - 1][arrPos.x]) &&
        (fieldStates.colContainsOnly(arrPos.x, fieldStates[arrPos.y][arrPos.x]))
    ) setWon(true)
    //Over Cross
    else if ((arrPos.y == arrPos.x || arrPos.y + arrPos.x == InitialGridSize - 1) &&
        (fieldStates.diagonallyContainsOnly(fieldStates[arrPos.y][arrPos.x]))
    ) setWon(true)
    //Draw
    else if (!fieldStates.deepContainsOnly(null)) setWon(false)
}