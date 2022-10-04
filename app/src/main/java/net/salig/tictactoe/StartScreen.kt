package net.salig.tictactoe

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import net.salig.tictactoe.ui.theme.Typography

@Composable
fun StartScreen(onNavigateToGameBoard: () -> Unit) {
    // A surface container using the 'background' color from the theme
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                stringResource(id = R.string.app_name),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize()
                    .padding(top = dimensionResource(id = R.dimen.padding_text)),
                style = Typography.h2
            )

            Image(
                painterResource(id = R.drawable.image),
                contentDescription = null,
                modifier = Modifier
                    .size(LocalConfiguration.current.screenWidthDp.dp)
                    .padding(dimensionResource(id = R.dimen.padding_default))
            )

            Button(
                interactionSource = MutableInteractionSource(),
                onClick = onNavigateToGameBoard
            )
            {
                Text(stringResource(id = R.string.start))
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_default)))
        }
    }
}