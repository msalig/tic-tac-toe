package net.salig.tictactoe.presentation.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import net.salig.tictactoe.R
import net.salig.tictactoe.core.component.LargeHeading
import net.salig.tictactoe.core.component.TicTacToeButton

@Composable
fun MenuScreen(onNavigateToGamemode: () -> Unit) {
    // A surface container using the 'background' color from the theme
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            LargeHeading(text = stringResource(id = R.string.app_name))

            Image(
                painterResource(id = R.drawable.image),
                contentDescription = null,
                modifier = Modifier
                    .size(LocalConfiguration.current.screenWidthDp.dp)
                    .padding(dimensionResource(id = R.dimen.padding_default))
            )
            TicTacToeButton(stringResource(id = R.string.start)) { onNavigateToGamemode() }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_default)))
        }
    }
}