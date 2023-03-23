package net.salig.tictactoe.core.component.game

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.delay
import net.salig.tictactoe.R
import net.salig.tictactoe.data.model.GameState

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
    state: GameState,
    hideDialog: () -> Unit,
    rematch: () -> Unit,
    navigateBack: () -> Unit,
) {
    AlertDialog(onDismissRequest = {
        // Dismiss the dialog when the user clicks outside the dialog or on the back
        // button. If you want to disable that functionality, simply use an empty
        // onCloseRequest.
    }, title = {
        Text(text = if (state.winningPlayer != null && state.winningPlayer == state.selfPlayerName)
            state.selfPlayerName + stringResource(id = R.string.won)
        else if (state.winningPlayer != null && state.winningPlayer == state.otherPlayerName)
            state.otherPlayerName + stringResource(id = R.string.won)
        else stringResource(id = R.string.draw))
    }, text = {
        Text(text = if (state.winningPlayer != null) stringResource(id = R.string.rematch)
        else stringResource(id = R.string.next_match))
    }, confirmButton = {
        TextButton(onClick = {
            rematch()
            hideDialog()
        }) {
            Text(stringResource(id = R.string.yes_rematch))
        }
    }, dismissButton = {
        TextButton(onClick = {
            hideDialog()
            navigateBack()
        }) {
            Text(stringResource(id = R.string.no))
        }
    })
}