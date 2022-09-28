package net.salig.tictactoe

import android.annotation.SuppressLint
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.tooling.preview.Preview
import net.salig.tictactoe.ui.theme.TicTacToeTheme

@Composable
fun RematchDialog(openDialog: MutableState<Boolean>) {
    AlertDialog(
        onDismissRequest = {
            // Dismiss the dialog when the user clicks outside the dialog or on the back
            // button. If you want to disable that functionality, simply use an empty
            // onCloseRequest.
            openDialog.value = false
        },
        title = {
            Text(text = "Player 1 Won")
        },
        text = {
            Text(
                "Do you want a rematch?"
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    openDialog.value = false
                }
            ) {
                Text("Yes, Rematch!")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    openDialog.value = false
                }
            ) {
                Text("No")
            }
        }
    )
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun RematchDialogPreview() {
    TicTacToeTheme {
        RematchDialog(mutableStateOf(true))
    }
}