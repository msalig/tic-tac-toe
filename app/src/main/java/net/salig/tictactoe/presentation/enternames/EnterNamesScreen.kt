package net.salig.tictactoe.presentation.enternames

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import net.salig.tictactoe.R
import net.salig.tictactoe.core.component.MediumHeading
import net.salig.tictactoe.core.component.TicTacToeButton
import net.salig.tictactoe.core.component.TicTacToeTextField
import net.salig.tictactoe.domain.validateTextFieldInput
import net.salig.tictactoe.presentation.game.GameScreenViewModel

@Composable
fun EnterNamesScreen(
    onNavigateToGameScreen: () -> Unit,
    viewModel: GameScreenViewModel = viewModel(),
) {
    var isErrorFieldOne by remember { mutableStateOf(false) }
    var isErrorFieldTwo by remember { mutableStateOf(false) }

    val context = LocalContext.current.applicationContext

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
        Column(
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            MediumHeading(text = stringResource(id = R.string.enter_name))

            TicTacToeTextField(
                label = stringResource(id = R.string.player_one),
                playerName = viewModel.gameState.selfPlayerName,
                isError = isErrorFieldOne,
                updateError = { isErrorFieldOne = it },
                updatePlayerName = {
                    viewModel.gameState = viewModel.gameState.copy(selfPlayerName = it)
                })

            TicTacToeTextField(
                label = stringResource(id = R.string.player_two),
                playerName = viewModel.gameState.otherPlayerName,
                isError = isErrorFieldTwo,
                updateError = { isErrorFieldTwo = it },
                updatePlayerName = {
                    viewModel.gameState = viewModel.gameState.copy(otherPlayerName = it)
                })

            TicTacToeButton(
                text = stringResource(id = R.string.play),
                enabled = !isErrorFieldOne && !isErrorFieldTwo
            ) {

                isErrorFieldOne = validateTextFieldInput(viewModel.gameState.selfPlayerName) != null
                isErrorFieldTwo =
                    validateTextFieldInput(viewModel.gameState.otherPlayerName) != null

                if (!isErrorFieldOne && !isErrorFieldTwo) {
                    onNavigateToGameScreen()

                    viewModel.gameState =
                        viewModel.gameState.copy(selfPlayerName = viewModel.gameState.selfPlayerName.ifEmpty {
                            context.getString(R.string.player_one)
                        }, otherPlayerName = viewModel.gameState.otherPlayerName.ifEmpty {
                            context.getString(R.string.player_two)
                        }, playerAtTurn = viewModel.gameState.selfPlayerName.ifEmpty {
                            context.getString(R.string.player_one)
                        })

                    viewModel.isHost = true
                }
            }
        }
    }
}