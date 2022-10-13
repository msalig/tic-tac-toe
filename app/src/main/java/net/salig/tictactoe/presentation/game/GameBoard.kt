package net.salig.tictactoe.presentation.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import net.salig.tictactoe.R

@Composable
fun GameBoard(
    gameBoardButtonsUIStates: LiveData<Array<Array<GameBoardButtonUIState>>>,
    screenWidth: Int,
    isShowDialog: Boolean,
    gridSize: Int,
    updateButtonUiState: (ItemPositionInArray) -> Unit,
) {
    Card(
        Modifier
            .size(screenWidth.dp)
            .padding(dimensionResource(id = R.dimen.padding_default))
    ) {
        val state = gameBoardButtonsUIStates.observeAsState().value

        LazyVerticalGrid(
            columns = GridCells.Fixed(gridSize),
            userScrollEnabled = false,
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Center
        ) {
            itemsIndexed(state!!) { rowIndex, row ->
                this@LazyVerticalGrid.itemsIndexed(row) { itemIndex, item ->
                    GameBoardButton(
                        item,
                        (screenWidth - 2 * dimensionResource(id = R.dimen.padding_default).value.toInt()) / gridSize
                    ) {
                        if (!isShowDialog)
                            updateButtonUiState(ItemPositionInArray(rowIndex, itemIndex))
                    }
                }
            }
        }
    }
}