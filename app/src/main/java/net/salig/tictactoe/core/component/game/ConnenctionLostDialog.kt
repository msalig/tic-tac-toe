package net.salig.tictactoe.core.component.game

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import net.salig.tictactoe.R

@Composable
fun ConnectionLostDialog(
    returnToMenu: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {
            // Dismiss the dialog when the user clicks outside the dialog or on the back
            // button. If you want to disable that functionality, simply use an empty
            // onCloseRequest.
        },
        title = {
            Text(
                text = stringResource(id = R.string.error)
            )
        },
        text = {
            Text(
                text = stringResource(id = R.string.error_connection_lost)
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    returnToMenu()
                }
            ) {
                Text(stringResource(id = R.string.ok))
            }
        }
    )
}