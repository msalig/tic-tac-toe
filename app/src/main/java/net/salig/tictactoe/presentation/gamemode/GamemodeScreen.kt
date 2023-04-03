package net.salig.tictactoe.presentation.gamemode

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import net.salig.tictactoe.R
import net.salig.tictactoe.core.component.TextFieldError
import net.salig.tictactoe.core.component.TicTacToeTextField
import net.salig.tictactoe.presentation.game.GameScreenViewModel

@Composable
fun GamemodeScreen(
    onNavigateToEnterNamesScreen: () -> Unit,
    onNavigateToGameScreen: () -> Unit,
    viewModel: GameScreenViewModel = viewModel(),
) {
    val context = LocalContext.current

    var error by remember { mutableStateOf(TextFieldError()) }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(25.dp)) {

            if (!viewModel.isLocalNetworkMultiplayer) {

                Button(modifier = Modifier.fillMaxWidth(),
                    onClick = { viewModel.isLocalNetworkMultiplayer = true }) {
                    Text(text = stringResource(id = R.string.local_network_multiplayer))
                }

                Button(modifier = Modifier.fillMaxWidth(),
                    onClick = { onNavigateToEnterNamesScreen() }) {
                    Text(text = stringResource(id = R.string.local_multiplayer))
                }

            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()) {

                    TicTacToeTextField().TextField(modifier = Modifier.fillMaxWidth(),
                        label = stringResource(id = R.string.player_name_placeholder),
                        playerName = viewModel.state.selfPlayerName,
                        isError = error.isError,
                        enabled = !viewModel.isHost && !viewModel.isJoin,
                        errorMessage = error.errorMessage,
                        updateError = { error = it },
                        updatePlayerName = {
                            viewModel.state = viewModel.state.copy(selfPlayerName = it)
                        })

                    Button(modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            if (!viewModel.isHost) {
                                viewModel.hostGame(context)
                            } else {
                                viewModel.stopHosting()
                                viewModel.isConnected = false
                            }
                        },
                        enabled = !viewModel.isJoin && viewModel.state.selfPlayerName.isNotEmpty()) {
                        Text(text = stringResource(id = if (!viewModel.isHost) R.string.host else R.string.hosting))
                    }

                    Button(modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            if (!viewModel.isJoin) {
                                viewModel.joinGame(context)
                            } else {
                                viewModel.stopJoining()
                                viewModel.isConnected = false
                            }
                        },
                        enabled = !viewModel.isHost && viewModel.state.selfPlayerName.isNotEmpty()) {
                        Text(text = stringResource(id = if (!viewModel.isJoin) R.string.join else R.string.joining))
                    }

                    Button(modifier = Modifier.fillMaxWidth(),
                        enabled = viewModel.isConnected && viewModel.state.selfPlayerName.isNotEmpty() && !error.isError,
                        onClick = {
                            viewModel.exchangeUsernames()

                            onNavigateToGameScreen()
                        }) {
                        Text(text = stringResource(id = R.string.start_game))
                    }

                    BackHandler {
                        viewModel.shutdown()
                    }
                }
            }
        }
    }
}