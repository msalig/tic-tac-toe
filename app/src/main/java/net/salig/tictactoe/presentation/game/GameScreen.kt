package net.salig.tictactoe.presentation.game

import android.os.Build
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import net.salig.tictactoe.R
import net.salig.tictactoe.core.InitialGridSize
import net.salig.tictactoe.core.component.RematchDialog
import net.salig.tictactoe.core.component.StartDialogDelay
import net.salig.tictactoe.core.component.TicTacToeButton

@Composable
fun GameScreen(
    playerNameOne: String?,
    playerNameTwo: String?,
    onNavigateToMenu: () -> Unit,
    viewModel: GameScreenViewModel = viewModel(),
) {

    val showDialogAfterDelay = remember {
        mutableStateOf(false)
    }

    // A surface container using the 'background' color from the theme
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {

            HighscoreCard(viewModel.highScorePlayerOne, viewModel.highScorePlayerTwo)

            //Adding PlayerProfileCards showing whose turn it is and the Players Icon
            Row(horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()) {
                PlayerProfileCard(Player.ONE, playerNameOne, getTurn = { viewModel.turn.value })

                PlayerProfileCard(Player.TWO, playerNameTwo, getTurn = { viewModel.turn.value })
            }

            //Adding the GameBoard
            GameBoard(viewModel.arrButtonUIStates,
                LocalConfiguration.current.screenWidthDp,
                viewModel.showDialog.value,
                InitialGridSize) { index ->
                when (viewModel.turn.value) {
                    1 -> viewModel.updateGameBoardButton(index, GameBoardButtonUIState.CROSS)
                    2 -> viewModel.updateGameBoardButton(index, GameBoardButtonUIState.CIRCLE)
                }
            }

            //Start the Delay and set the variable to show the dialog afterwards on true
            StartDialogDelay(isShowDialog = viewModel.showDialog.value,
                setShowDialogAfterDelay = { showDialogAfterDelay.value = it })

            //Show the Dialog if showDialogAfterDelay is true
            if (showDialogAfterDelay.value) RematchDialog(playerNameOne,
                playerNameTwo,
                viewModel.winner.value,
                hideDialog = {
                    viewModel.showDialog.value = false
                    showDialogAfterDelay.value = false
                },
                navigateBack = onNavigateToMenu,
                resetButtonUiStates = {
                    viewModel._arrButtonUIStates.value =
                        GameBoardButtonUIState.resetArrButtonUIStates(); viewModel.buttonsAlreadyClicked =
                    0
                })

            //Adding a Return-to-Menu-Button if the software buttons on the phone aren't activated by default
            if (Build.VERSION.SDK_INT >= 31) {
                TicTacToeButton(stringResource(id = R.string.back)) { onNavigateToMenu() }
            } else Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_default)))
        }
    }
}