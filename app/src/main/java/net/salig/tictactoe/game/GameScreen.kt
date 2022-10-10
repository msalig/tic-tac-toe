package net.salig.tictactoe.game

import android.os.Build
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import net.salig.tictactoe.R
import net.salig.tictactoe.ui.theme.CircleBlue
import net.salig.tictactoe.ui.theme.CrossRed
import net.salig.tictactoe.util.RematchDialog

@Composable
fun GameScreen(
    playerNameOne: String?,
    playerNameTwo: String?,
    onNavigateToMenu: () -> Unit,
    viewModel: GameScreenViewModel = viewModel()
) {

    val anim = remember {
        Animatable(0f)
    }

    viewModel.gameBoardSizeDp =
        (LocalConfiguration.current.screenWidthDp - 2 * dimensionResource(id = R.dimen.padding_default).value).toInt()
    viewModel.gameBoardSizePx = LocalDensity.current.run { viewModel.gameBoardSizeDp.dp.toPx() }

    val showDialogAfterDelay = remember {
        mutableStateOf(false)
    }

    // A surface container using the 'background' color from the theme
    Surface(
        modifier = Modifier
            .fillMaxSize(), color = MaterialTheme.colors.background
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            GameScreenTop(
                playerNameOne, playerNameTwo,
                getTurn = { viewModel.turn.value },
                viewModel.highScorePlayerOne, viewModel.highScorePlayerTwo
            )

            GameBoard(
                anim,
                viewModel.wonDirection,
                viewModel.winner.value,
                viewModel.lineCoordinates,
                viewModel.buttonUiStates,
                LocalConfiguration.current.screenWidthDp,
                viewModel.gridSize
            ) { index ->
                if (!viewModel.showDialog.value) {
                    if (viewModel.turn.value == 1) {
                        viewModel.updateGameBoardButton(
                            index,
                            ButtonUiState(R.drawable.ic_cross, CrossRed, R.string.cross, false)
                        )
                    } else {
                        viewModel.updateGameBoardButton(
                            index,
                            ButtonUiState(R.drawable.ic_circle, CircleBlue, R.string.circle, false)
                        )
                    }
                }

            }

            //Showing the RematchDialog, if someone won or draw
            if (viewModel.showDialog.value) {
                LaunchedEffect(viewModel.showDialog.value) {
                    delay(500L)
                    showDialogAfterDelay.value = true
                }
            }

            if (showDialogAfterDelay.value)
                RematchDialog(viewModel.winner.value,
                    hideDialog = {
                        viewModel.showDialog.value = false
                        showDialogAfterDelay.value = false
                    },
                    setTurn = { player -> viewModel.turn.value = player },
                    navigateBack = onNavigateToMenu,
                    resetButtonUiStates = { viewModel.resetButtonUiStates() })


            //Adding a Return-to-Menu-Button if the software buttons on the phone aren't activated by default
            if (Build.VERSION.SDK_INT >= 31) {
                Button(
                    interactionSource = MutableInteractionSource(),
                    onClick = onNavigateToMenu,
                    modifier = Modifier.padding(
                        bottom = dimensionResource(
                            id = R.dimen.padding_small
                        )
                    )
                ) {
                    Text(stringResource(id = R.string.back))
                }
            } else
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_default)))
        }
    }
}

@Composable
fun GameScreenTop(
    playerNameOne: String?,
    playerNameTwo: String?,
    getTurn: () -> Int,
    highScorePlayerOne: Int,
    highScorePlayerTwo: Int
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
            Text(
                text = "$highScorePlayerOne",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(
                    bottom = dimensionResource(
                        id = R.dimen.padding_text_small
                    )
                )
            )
            Text(
                text = stringResource(id = R.string.highscore),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(
                    bottom = dimensionResource(
                        id = R.dimen.padding_text_small
                    )
                )
            )
            Text(
                text = "$highScorePlayerTwo",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(
                    bottom = dimensionResource(
                        id = R.dimen.padding_text_small
                    )
                )
            )
        }
    }

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()
    ) {

        //Player 1 Icon-and-Name Card
        Card(
            elevation = dimensionResource(id = R.dimen.elevation_default),
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.rounded_corners)),
            backgroundColor = if (getTurn() == 1) MaterialTheme.colors.secondary else MaterialTheme.colors.surface,
            modifier = Modifier
                .height(dimensionResource(id = R.dimen.player_card_height))
                .width(dimensionResource(id = R.dimen.player_card_width))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_text))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_cross),
                    contentDescription = stringResource(id = R.string.cross)
                )
                Text(
                    text = StringBuilder().append(if (playerNameOne == "") stringResource(id = R.string.player_one) else playerNameOne)
                        .append(
                            if (getTurn() == 1) stringResource(
                                id = R.string.turn
                            ) else stringResource(id = R.string.nothing)
                        ).toString(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = dimensionResource(id = R.dimen.padding_text))
                )
            }
        }

        //Player 2 Icon-and-Name Card
        Card(
            elevation = dimensionResource(id = R.dimen.elevation_default),
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.rounded_corners)),
            backgroundColor = if (getTurn() == 2) MaterialTheme.colors.secondary else MaterialTheme.colors.surface,
            modifier = Modifier
                .height(dimensionResource(id = R.dimen.player_card_height))
                .width(dimensionResource(id = R.dimen.player_card_width))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_text))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_circle),
                    contentDescription = stringResource(id = R.string.circle)
                )
                Text(
                    text = StringBuilder().append(if (playerNameTwo == "") stringResource(id = R.string.player_two) else playerNameTwo)
                        .append(
                            if (getTurn() == 2) stringResource(
                                id = R.string.turn
                            ) else stringResource(id = R.string.nothing)
                        ).toString(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = dimensionResource(id = R.dimen.padding_text))
                )
            }
        }
    }
}

@Composable
fun GameBoard(
    animVal: Animatable<Float, AnimationVector1D>,
    wonDirection: String,
    winner: Int,
    list: List<Float>,
    buttons: List<ButtonUiState>,
    screenWidth: Int,
    gridSize: Int,
    updateButtonUiState: (Int) -> Unit
) {

    Card(
        Modifier
            .size(screenWidth.dp)
            .padding(
                dimensionResource(id = R.dimen.padding_default)
            )
    ) {
        LazyHorizontalGrid(
            rows = GridCells.Fixed(gridSize),
            userScrollEnabled = false,
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Center
        ) {
            itemsIndexed(buttons) { index, item ->
                GameBoardButton(
                    item.drawable,
                    item.tint,
                    item.contentDescription,
                    item.enabled,
                    (screenWidth - 2 * dimensionResource(id = R.dimen.padding_default).value.toInt()) / gridSize
                ) {
                    updateButtonUiState(index)
                }
            }
        }

        LaunchedEffect(winner) {
            animVal.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 500, easing = LinearEasing)
            )

            if (!animVal.isRunning) {
                delay(1000L)
                animVal.snapTo(0f)
            }
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            drawLine(
                color = if (winner == R.string.player_one) CrossRed else CircleBlue,
                start = Offset(x = list[0], y = list[1]),
                end =
                when (wonDirection) {
                    "|" -> {
                        Offset(x = list[2], y = list[3] * animVal.value)
                    }
                    "-" -> {
                        Offset(x = list[2] * animVal.value, y = list[3])
                    }
                    "/" -> {
                        Offset(x = list[2], y = list[3])
                    }
                    "\\" -> {
                        Offset(x = list[2] * animVal.value, y = list[3] * animVal.value)
                    }
                    else -> {
                        Offset(x = list[2], y = list[3])
                    }
                },
                strokeWidth = 10f
            )
        }
    }
}

@Composable
fun GameBoardButton(
    drawableRes: Int,
    tint: Color,
    contentDescription: Int,
    enabled: Boolean,
    buttonSize: Int,
    onButtonUpdate: () -> Unit
) {
    IconButton(
        interactionSource = MutableInteractionSource(), onClick = {
            if (enabled) onButtonUpdate()
        }, modifier = Modifier
            .size(buttonSize.dp)
            .border(
                dimensionResource(id = R.dimen.border_size), MaterialTheme.colors.secondary
            )
    ) {
        Icon(
            painter = painterResource(id = drawableRes),
            tint = tint,
            contentDescription = stringResource(id = contentDescription),
            modifier = Modifier.size((buttonSize * 0.75).dp)
        )
    }
}