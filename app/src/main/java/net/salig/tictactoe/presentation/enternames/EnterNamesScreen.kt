package net.salig.tictactoe.presentation.enternames

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import net.salig.tictactoe.R
import net.salig.tictactoe.core.component.MediumHeading
import net.salig.tictactoe.core.component.TicTacToeButton
import net.salig.tictactoe.core.component.TicTacToeTextField
import net.salig.tictactoe.data.model.TextFieldError
import net.salig.tictactoe.presentation.game.GameScreenViewModel

@Composable
fun EnterNamesScreen(
    onNavigateToGameScreen: () -> Unit,
    viewModel: GameScreenViewModel = viewModel(),
) {
    val fieldOne by remember { mutableStateOf(TicTacToeTextField()) }
    val fieldTwo by remember { mutableStateOf(TicTacToeTextField()) }
    var error by remember { mutableStateOf(TextFieldError()) }
    var error2 by remember { mutableStateOf(TextFieldError()) }


    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
        Column(horizontalAlignment = CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly) {

            MediumHeading(text = stringResource(id = R.string.enter_name))

            fieldOne.TextField(label = stringResource(id = R.string.player_one),
                playerName = viewModel.state.selfPlayerName,
                isError = error.isError,
                errorMessage = error.errorMessage,
                updateError = { error = it },
                updatePlayerName = { viewModel.state = viewModel.state.copy(selfPlayerName = it) })

            fieldTwo.TextField(label = stringResource(id = R.string.player_two),
                playerName = viewModel.state.otherPlayerName,
                isError = error2.isError,
                errorMessage = error2.errorMessage,
                updateError = { error2 = it },
                updatePlayerName = { viewModel.state = viewModel.state.copy(otherPlayerName = it) })

            TicTacToeButton(stringResource(id = R.string.play),
                enabled = !error.isError && !error2.isError) {
                error = fieldOne.validate(viewModel.state.selfPlayerName)
                error2 = fieldTwo.validate(viewModel.state.otherPlayerName)

                if (!error.isError && !error2.isError) {
                    onNavigateToGameScreen()

                    viewModel.state =
                        viewModel.state.copy(selfPlayerName = viewModel.state.selfPlayerName.ifEmpty { fieldOne.label },
                            otherPlayerName = viewModel.state.otherPlayerName.ifEmpty { fieldTwo.label },
                            playerAtTurn = viewModel.state.selfPlayerName.ifEmpty { fieldOne.label })

                    viewModel.isHost = true
                }
            }
        }
    }
}