package net.salig.tictactoe

import android.annotation.SuppressLint
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import net.salig.tictactoe.ui.theme.TicTacToeTheme

@Composable
fun RematchDialog(
    winner: Int,
    hideDialog: () -> Unit,
    setTurn: (Int) -> Unit,
    resetButtonUiStates: () -> Unit,
    navigateBack: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            // Dismiss the dialog when the user clicks outside the dialog or on the back
            // button. If you want to disable that functionality, simply use an empty
            // onCloseRequest.
        },
        title = {
            Text(
                text = if (winner != R.string.draw) stringResource(id = winner) + stringResource(id = R.string.won)
                else stringResource(id = R.string.draw)
            )
        },
        text = {
            Text(
                stringResource(id = R.string.rematch)
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    when (winner) {
                        R.string.player_one -> setTurn(2)
                        R.string.player_two -> setTurn(1)
                    }
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
                    //openDialog.value = false
                    navigateBack()
                }
            ) {
                Text(stringResource(id = R.string.no))
            }
        }
    )
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun RematchDialogPreview() {
    TicTacToeTheme {
        RematchDialog(R.string.nothing, { }, {}, {}, {})
    }
}