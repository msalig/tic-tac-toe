package net.salig.tictactoe.presentation.enternames

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import net.salig.tictactoe.R
import net.salig.tictactoe.core.component.MediumHeading
import net.salig.tictactoe.core.component.TicTacToeButton
import net.salig.tictactoe.core.component.TicTacToeTextField

@Composable
fun EnterNamesScreen(
    onNavigateToGameScreen: (String?, String?) -> Unit,
    viewModel: EnterNamesViewModel = viewModel(),
) {
    val fieldOne = remember { mutableStateOf(TicTacToeTextField()) }
    val fieldTwo = remember { mutableStateOf(TicTacToeTextField()) }

    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
    ) {
        Column(
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            MediumHeading(text = stringResource(id = R.string.enter_name))

            fieldOne.value.TextField(
                label = stringResource(id = R.string.player_one),
                playerName = viewModel.name,
                isError = viewModel.error.isError,
                errorMessage = viewModel.error.errorMessage,
                updateError = { viewModel.error = it },
                updatePlayerName = { viewModel.name = it }
            )

            fieldTwo.value.TextField(
                label = stringResource(id = R.string.player_two),
                playerName = viewModel.name2,
                isError = viewModel.error2.isError,
                errorMessage = viewModel.error2.errorMessage,
                updateError = { viewModel.error2 = it },
                updatePlayerName = { viewModel.name2 = it }
            )

            TicTacToeButton(
                stringResource(id = R.string.play),
                enabled = !viewModel.error.isError && !viewModel.error2.isError
            )
            {
                viewModel.error = fieldOne.value.validate(viewModel.name)
                viewModel.error2 = fieldTwo.value.validate(viewModel.name2)

                if (!viewModel.error.isError && !viewModel.error2.isError) {
                    onNavigateToGameScreen(
                        viewModel.name.ifEmpty { fieldOne.value.label },
                        viewModel.name2.ifEmpty { fieldTwo.value.label }
                    )
                }
            }
        }
    }
}