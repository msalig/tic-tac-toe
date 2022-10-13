package net.salig.tictactoe.presentation.game

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import net.salig.tictactoe.R
import net.salig.tictactoe.core.component.HighscoreCardText

@Composable
fun HighscoreCard(
    highScorePlayerOne: Int,
    highScorePlayerTwo: Int,
) {
    Card(
        backgroundColor = MaterialTheme.colors.primaryVariant,
        elevation = dimensionResource(id = R.dimen.elevation_default),
        shape = RoundedCornerShape(
            bottomStart = dimensionResource(id = R.dimen.rounded_corners),
            bottomEnd = dimensionResource(id = R.dimen.rounded_corners)
        ),
        modifier = Modifier
            .height(dimensionResource(id = R.dimen.highscore_card_height))
            .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(
                horizontal = dimensionResource(id = R.dimen.padding_default) + (dimensionResource(
                    id = R.dimen.player_card_width
                ) / 6)
            )
        ) {
            HighscoreCardText(text = "$highScorePlayerOne")

            HighscoreCardText(stringResource(id = R.string.highscore))

            HighscoreCardText(text = "$highScorePlayerTwo")
        }
    }
}