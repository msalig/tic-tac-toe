package net.salig.tictactoe.core.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import net.salig.tictactoe.R
import net.salig.tictactoe.core.theme.Typography

@Composable
fun HighscoreCardText(text: String) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        modifier = Modifier.padding(
            bottom = dimensionResource(
                id = R.dimen.padding_text_small
            )
        )
    )
}

@Composable
fun LargeHeading(text: String) {
    Text(
        text,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize()
            .padding(top = dimensionResource(id = R.dimen.padding_text)),
        style = Typography.h2
    )
}

@Composable
fun MediumHeading(text: String) {
    Text(
        text,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize()
            .padding(dimensionResource(id = R.dimen.padding_text)),
        style = Typography.h4
    )
}