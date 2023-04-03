package net.salig.tictactoe.presentation.game

import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import net.salig.tictactoe.R
import net.salig.tictactoe.core.Constants.InitialGridSize
import net.salig.tictactoe.core.component.LoadingIndicator
import net.salig.tictactoe.core.component.TicTacToeButton
import net.salig.tictactoe.core.component.game.*
import net.salig.tictactoe.data.model.Player

@Composable
fun GameScreen(
    onNavigateToMenu: () -> Unit,
    viewModel: GameScreenViewModel = viewModel(),
) {

    if (viewModel.isLocalNetworkMultiplayer && (viewModel.state.otherPlayerName.isEmpty() || viewModel.isWaitingForRematchResponse)) {
        LoadingIndicator()

        //TODO: BackHandler in LoadingIndicator
        BackHandler() {
            onNavigateToMenu()
            viewModel.shutdown()
        }
    }

    // A surface container using the 'background' color from the theme
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            if (viewModel.isHost) {
                HighscoreCard(highScorePlayerOne = viewModel.state.selfPlayerHighscore,
                    highScorePlayerTwo = viewModel.state.otherPlayerHighscore)
            } else {
                HighscoreCard(highScorePlayerOne = viewModel.state.otherPlayerHighscore,
                    highScorePlayerTwo = viewModel.state.selfPlayerHighscore)
            }

            //Adding PlayerProfileCards showing whose turn it is and the Players Icon
            Row(horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()) {
                PlayerProfileCard(Player.ONE,
                    if (viewModel.isHost) {
                        viewModel.state.selfPlayerName
                    } else {
                        viewModel.state.otherPlayerName
                    }, getTurn = { viewModel.state.playerAtTurn })

                PlayerProfileCard(Player.TWO,
                    if (viewModel.isHost) {
                        viewModel.state.otherPlayerName
                    } else {
                        viewModel.state.selfPlayerName
                    }, getTurn = { viewModel.state.playerAtTurn })
            }

            //Adding the GameBoard
            GameBoard(state = viewModel.state,
                screenWidth = LocalConfiguration.current.screenWidthDp,
                isLocalNetworkMultiplayer = viewModel.isLocalNetworkMultiplayer,
                gridSize = InitialGridSize,
                onTapInField = { index ->
                    viewModel.finishTurn(index)
                })

            //Start the Delay and set the variable to show the dialog afterwards on true
            StartDialogDelay(isShowDialog = viewModel.startRematchDialogDelay,
                setShowDialogAfterDelay = { viewModel.showRematchDialog = it })

            //Show the RematchDialog if showDialogAfterDelay is true
            if (viewModel.showRematchDialog) {
                RematchDialog(viewModel.state, hideDialog = {
                    viewModel.startRematchDialogDelay = false
                    viewModel.showRematchDialog = false
                }, navigateBack = {
                    viewModel.shutdown()
                    onNavigateToMenu()
                }, rematch = viewModel::rematch)
            }


            //Showing an Dialog when the connection to the other player is lost
            if (!viewModel.isConnected && viewModel.isLocalNetworkMultiplayer) {
                ConnectionLostDialog {
                    viewModel.startRematchDialogDelay = false
                    viewModel.showRematchDialog = false
                    viewModel.shutdown()
                    onNavigateToMenu()
                }
            }

            //Adding a Return-to-Menu-Button if the software buttons on the phone aren't activated by default
            if (Settings.Secure.getInt(LocalContext.current.contentResolver,
                    "navigation_mode",
                    0) == 2
            ) {
                TicTacToeButton(text = stringResource(id = R.string.back)) { onNavigateToMenu() }
            } else {
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_default)))
            }
        }

        BackHandler {
            viewModel.shutdown()
            onNavigateToMenu()
        }
    }
}