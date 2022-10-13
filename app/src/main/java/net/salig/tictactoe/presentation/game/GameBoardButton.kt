package net.salig.tictactoe.presentation.game

import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import net.salig.tictactoe.R

@Composable
fun GameBoardButton(
    buttonUIState: GameBoardButtonUIState,
    buttonSize: Int,
    onButtonUpdate: () -> Unit,
) {
    IconButton(
        interactionSource = MutableInteractionSource(), onClick = {
            if (buttonUIState.enabled) onButtonUpdate()
        }, modifier = Modifier
            .size(buttonSize.dp)
            .border(dimensionResource(id = R.dimen.border_size), MaterialTheme.colors.secondary)
    ) {
        Icon(
            painter = painterResource(id = buttonUIState.drawable),
            tint = buttonUIState.tint,
            contentDescription = stringResource(id = buttonUIState.contentDescription),
            modifier = Modifier.size((buttonSize * 0.75).dp)
        )
    }
}