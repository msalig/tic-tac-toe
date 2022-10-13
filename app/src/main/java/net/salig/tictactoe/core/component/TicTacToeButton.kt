package net.salig.tictactoe.core.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import net.salig.tictactoe.R

@Composable
fun TicTacToeButton(text: String, enabled: Boolean = true, onCLick: () -> Unit) {
    Button(
        interactionSource = MutableInteractionSource(),
        onClick = onCLick,
        enabled = enabled,
        modifier = Modifier.padding(
            bottom = dimensionResource(
                id = R.dimen.padding_small
            )
        )
    ) {
        Text(text)
    }
}