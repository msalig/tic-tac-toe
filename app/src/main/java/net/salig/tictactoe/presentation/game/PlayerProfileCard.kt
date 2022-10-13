package net.salig.tictactoe.presentation.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import net.salig.tictactoe.R

@Composable
fun PlayerProfileCard(
    player: Player,
    playerName: String?,
    getTurn: () -> Int,
) {
    Card(elevation = dimensionResource(id = R.dimen.elevation_default),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.rounded_corners)),
        backgroundColor = if (getTurn() == player.playerNumber) MaterialTheme.colors.secondary else MaterialTheme.colors.surface,
        modifier = Modifier
            .height(dimensionResource(id = R.dimen.player_card_height))
            .width(dimensionResource(id = R.dimen.player_card_width))) {

        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_text))) {

            Image(painter = painterResource(id = player.drawableRes),
                contentDescription = stringResource(id = player.drawableContentDescription))

            Text(text = StringBuilder().append(playerName)
                .append(if (getTurn() == player.playerNumber) stringResource(id = R.string.turn) else stringResource(
                    id = R.string.nothing)).toString(),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = dimensionResource(id = R.dimen.padding_text)))
        }
    }
}