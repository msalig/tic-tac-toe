package net.salig.tictactoe.enternames

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import net.salig.tictactoe.R
import net.salig.tictactoe.ui.theme.Typography

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EnterNamesScreen(
    onNavigateToGameScreen: (String?, String?) -> Unit,
    viewModel: EnterNamesViewModel = viewModel()
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Surface(
        modifier = Modifier
            .fillMaxSize(), color = MaterialTheme.colors.background
    ) {
        Column(
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            Text(
                stringResource(id = R.string.enter_name),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize()
                    .align(CenterHorizontally)
                    .padding(dimensionResource(id = R.dimen.padding_text)),
                style = Typography.h4
            )
            Column {
                OutlinedTextField(
                    value = if (viewModel.name != "") viewModel.name else "",
                    onValueChange = {
                        viewModel.name = it
                        viewModel.isError = false
                    },
                    trailingIcon = {
                        if (viewModel.isError)
                            Icon(Icons.Default.Warning, "error", tint = MaterialTheme.colors.error)
                    },
                    singleLine = true,
                    label = {
                        Text(
                            if (viewModel.isError) "${stringResource(id = R.string.player_one)} *" else stringResource(
                                id = R.string.player_one
                            )
                        )
                    },
                    isError = viewModel.isError,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            viewModel.isError = viewModel.validate(viewModel.name, 1)
                        },
                    )
                )
                if (viewModel.isError) {
                    Text(
                        text = stringResource(id = viewModel.errorMessage),
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
            Column {
                OutlinedTextField(
                    value = if (viewModel.name2 != "") viewModel.name2 else "",
                    onValueChange = {
                        viewModel.name2 = it
                        viewModel.isError2 = false
                    },
                    trailingIcon = {
                        if (viewModel.isError2)
                            Icon(Icons.Default.Warning, "error", tint = MaterialTheme.colors.error)
                    },
                    singleLine = true,
                    label = {
                        Text(
                            if (viewModel.isError2) "${stringResource(id = R.string.player_two)} *" else stringResource(
                                id = R.string.player_two
                            )
                        )
                    },
                    isError = viewModel.isError2,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            viewModel.isError2 = viewModel.validate(viewModel.name2, 2)
                        },
                    )
                )
                if (viewModel.isError2) {
                    Text(
                        text = stringResource(id = viewModel.errorMessage2),
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
            Button(
                interactionSource = MutableInteractionSource(),
                onClick = {
                    viewModel.isError = viewModel.validate(viewModel.name, 1)
                    viewModel.isError2 = viewModel.validate(viewModel.name2, 2)
                    if (!viewModel.isError && !viewModel.isError2) {
                        onNavigateToGameScreen(viewModel.name, viewModel.name2)
                    }
                },
                enabled = !viewModel.isError && !viewModel.isError2
            )
            {
                Text(stringResource(id = R.string.play))
            }
        }
    }
}