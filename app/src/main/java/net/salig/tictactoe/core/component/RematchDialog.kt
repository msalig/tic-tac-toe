package net.salig.tictactoe.core.component

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.delay
import net.salig.tictactoe.R
import net.salig.tictactoe.presentation.game.Player

@Composable
fun StartDialogDelay(isShowDialog: Boolean, setShowDialogAfterDelay: (Boolean) -> Unit) {
    if (isShowDialog) {
        LaunchedEffect(Unit) {
            delay(500L)
            setShowDialogAfterDelay(true)
        }
    }
}

@Composable
fun RematchDialog(
    playerNameOne: String?,
    playerNameTwo: String?,
    winner: Int,
    hideDialog: () -> Unit,
    resetButtonUiStates: () -> Unit,
    navigateBack: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {
            // Dismiss the dialog when the user clicks outside the dialog or on the back
            // button. If you want to disable that functionality, simply use an empty
            // onCloseRequest.
        },
        title = {
            Text(
                text = if (winner != R.string.draw && winner == Player.ONE.initialPlayerName) playerNameOne + stringResource(
                    id = R.string.won)
                else if (winner != R.string.draw && winner == Player.TWO.initialPlayerName) playerNameTwo + stringResource(
                    id = R.string.won)
                else stringResource(id = R.string.draw)
            )
        },
        text = {
            Text(
                text = if (winner != R.string.draw) stringResource(id = R.string.rematch)
                else stringResource(id = R.string.next_match)
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    resetButtonUiStates()
                    hideDialog()
                }
            ) {
                Text(stringResource(id = R.string.yes_rematch))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    hideDialog()
                    navigateBack()
                }
            ) {
                Text(stringResource(id = R.string.no))
            }
        }
    )
}