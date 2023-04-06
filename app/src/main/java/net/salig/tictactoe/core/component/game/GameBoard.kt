package net.salig.tictactoe.core.component.game

import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import net.salig.tictactoe.R
import net.salig.tictactoe.data.model.GameBoardButtonUIStates
import net.salig.tictactoe.data.model.GameState
import net.salig.tictactoe.data.serialization.model.Coordinates

@Composable
fun GameBoard(
    state: GameState,
    screenWidth: Int,
    isLocalNetworkMultiplayer: Boolean,
    gridSize: Int,
    onTapInField: (Coordinates) -> Unit,
) {
    val buttonSize =
        (screenWidth - 2 * dimensionResource(id = R.dimen.padding_default).value.toInt()) / gridSize

    fun isOnClickEnabled(buttonUIState: GameBoardButtonUIStates) =
        buttonUIState == GameBoardButtonUIStates.NOTHING &&
                (!isLocalNetworkMultiplayer || state.playerAtTurn == state.selfPlayerName)

    Card(
        Modifier
            .size(screenWidth.dp)
            .padding(dimensionResource(id = R.dimen.padding_default))
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(gridSize),
            userScrollEnabled = false,
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Center
        ) {

            itemsIndexed(state.field) { rowIndex, row ->
                this@LazyVerticalGrid.itemsIndexed(row) { itemIndex, item ->

                    val buttonState = when (item) {
                        'X' -> GameBoardButtonUIStates.CROSS
                        'O' -> GameBoardButtonUIStates.CIRCLE
                        else -> GameBoardButtonUIStates.NOTHING
                    }

                    IconButton(
                        interactionSource = MutableInteractionSource(), onClick = {
                            if (isOnClickEnabled(buttonState)) {
                                onTapInField(Coordinates(itemIndex, rowIndex))
                            }
                        }, modifier = Modifier
                            .size(buttonSize.dp)
                            .border(dimensionResource(id = R.dimen.border_size),
                                MaterialTheme.colors.secondary)
                    ) {
                        Icon(
                            painter = painterResource(id = buttonState.drawable),
                            tint = buttonState.tint,
                            contentDescription = stringResource(id = buttonState.contentDescription),
                            modifier = Modifier.size((buttonSize * 0.75).dp)
                        )
                    }
                }
            }
        }
    }
}