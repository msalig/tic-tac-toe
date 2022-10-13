package net.salig.tictactoe.core.component

import androidx.annotation.StringRes
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import net.salig.tictactoe.R

class TicTacToeTextField {

    fun validate(text: String): TextFieldError {
        if (text.length > 15)
            return TextFieldError(R.string.error_message_length, true)
        else if (!text.matches(Regex("^[a-zA-Z0-9. ]*\$")))
            return TextFieldError(R.string.error_message_symbols, true)

        return TextFieldError(
            R.string.nothing,
            !(text.matches(Regex("^[a-zA-Z0-9. ]*\$")) && text.length < 15)
        )
    }

    lateinit var label: String

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun TextField(
        label: String,
        playerName: String,
        isError: Boolean,
        @StringRes errorMessage: Int,
        updateError: (TextFieldError) -> Unit,
        updatePlayerName: (String) -> Unit,
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        val scope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            this@TicTacToeTextField.label = label
        }

        Column {
            OutlinedTextField(
                value = playerName,
                placeholder = {
                    Text(label)
                },
                onValueChange = {
                    updatePlayerName(it)
                    updateError(TextFieldError(R.string.nothing, false))
                },
                trailingIcon = {
                    if (isError)
                        Icon(Icons.Default.Warning,
                            stringResource(id = R.string.error),
                            tint = MaterialTheme.colors.error)
                },
                singleLine = true,
                label = {
                    Text(
                        if (isError) "$label *" else label
                    )
                },
                isError = isError,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        scope.launch {
                            updateError(validate(playerName))
                        }
                    },
                )
            )
            if (isError) {
                Text(
                    text = stringResource(id = errorMessage),
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}