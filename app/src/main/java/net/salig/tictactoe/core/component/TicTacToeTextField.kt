package net.salig.tictactoe.core.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import net.salig.tictactoe.R
import net.salig.tictactoe.domain.validateTextFieldInput

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TicTacToeTextField(
    modifier: Modifier = Modifier,
    label: String,
    playerName: String,
    isError: Boolean,
    enabled: Boolean = true,
    updateError: (Boolean) -> Unit,
    updatePlayerName: (String) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()

    var errorMessage: Int? by remember { mutableStateOf(null) }

    Column(modifier = modifier) {
        OutlinedTextField(enabled = enabled,
            modifier = modifier,
            value = playerName,
            placeholder = {
                Text(label)
            },
            onValueChange = {
                updatePlayerName(it)
                errorMessage = validateTextFieldInput(it)
                updateError(errorMessage != null)
            },
            trailingIcon = {
                if (isError) Icon(Icons.Default.Warning,
                    stringResource(id = R.string.error),
                    tint = MaterialTheme.colors.error)
            },
            singleLine = true,
            label = {
                Text(if (isError) "$label *" else label)
            },
            isError = isError,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    scope.launch {
                        errorMessage = validateTextFieldInput(playerName)
                        updateError(errorMessage != null)
                    }
                },
            ))
        if (isError && errorMessage != null) {
            Text(text = stringResource(id = errorMessage!!),
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(start = 16.dp))
        }
    }
}