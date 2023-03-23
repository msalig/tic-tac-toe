package net.salig.tictactoe.presentation.game

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
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
    playerNameOne: String,
    playerNameTwo: String,
    onNavigateToMenu: () -> Unit,
    viewModel: GameScreenViewModel = viewModel(),
) {
    LaunchedEffect(!viewModel.isGameScreenOnCreate && !viewModel.isLocalNetworkMultiplayer) {
        viewModel.state = viewModel.state.copy(selfPlayerName = playerNameOne,
            otherPlayerName = playerNameTwo,
            playerAtTurn = playerNameOne)
        viewModel.isGameScreenOnCreate = true
    }

    if (viewModel.isLocalNetworkMultiplayer && (viewModel.state.otherPlayerName.isEmpty() || viewModel.isWaitingForRematchResponse)) {
        LoadingIndicator(Modifier)
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
                PlayerProfileCard(Player.ONE, if (playerNameOne == "null") {
                    if (viewModel.isHost) {
                        viewModel.state.selfPlayerName
                    } else {
                        viewModel.state.otherPlayerName
                    }
                } else playerNameOne, getTurn = { viewModel.state.playerAtTurn })

                PlayerProfileCard(Player.TWO, if (playerNameTwo == "null") {
                    if (viewModel.isHost) {
                        viewModel.state.otherPlayerName
                    } else {
                        viewModel.state.selfPlayerName
                    }
                } else playerNameTwo, getTurn = { viewModel.state.playerAtTurn })
            }

            //Adding the GameBoard
            GameBoard(state = viewModel.state,
                screenWidth = LocalConfiguration.current.screenWidthDp,
                isShowDialog = viewModel.showRematchDialog,
                gridSize = InitialGridSize,
                onTapInField = { index ->
                    viewModel.finishTurn(index)
                })

            //Start the Delay and set the variable to show the dialog afterwards on true
            StartDialogDelay(isShowDialog = viewModel.showRematchDialog,
                setShowDialogAfterDelay = { viewModel.showRematchDialogAfterDelay = it })

            //Show the RematchDialog if showDialogAfterDelay is true
            if (viewModel.showRematchDialogAfterDelay) {
                RematchDialog(viewModel.state, hideDialog = {
                    viewModel.showRematchDialog = false
                    viewModel.showRematchDialogAfterDelay = false
                }, navigateBack = {
                    onNavigateToMenu()
                    viewModel.shutdown()
                }, rematch = viewModel::rematch)
            }


            //Showing an Dialog when the connection to the other player is lost
            if (!viewModel.isConnected && viewModel.isLocalNetworkMultiplayer) {
                ConnectionLostDialog {
                    onNavigateToMenu()
                    viewModel.shutdown()
                }
            }

            //Adding a Return-to-Menu-Button if the software buttons on the phone aren't activated by default
            if (Build.VERSION.SDK_INT >= 31) {
                TicTacToeButton(stringResource(id = R.string.back)) { onNavigateToMenu() }
            } else Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_default)))
        }

        BackHandler {
            onNavigateToMenu()
            viewModel.shutdown()
        }
    }
}