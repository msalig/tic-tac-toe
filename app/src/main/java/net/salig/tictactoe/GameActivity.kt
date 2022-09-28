package net.salig.tictactoe

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.salig.tictactoe.ui.theme.CircleBlue
import net.salig.tictactoe.ui.theme.CrossRed
import net.salig.tictactoe.ui.theme.TicTacToeTheme

class GameActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
        }
    }
}

@Composable
fun GameScreen(onNavigateToMenu: () -> Unit) {
    var turn by remember { mutableStateOf(1) }

    // A surface container using the 'background' color from the theme
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Card(
                    elevation = 3.dp,
                    shape = RoundedCornerShape(16.dp),
                    backgroundColor = if (turn == 1) MaterialTheme.colors.secondary else MaterialTheme.colors.surface,
                    modifier = Modifier
                        .height(150.dp)
                        .width(120.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top,
                        modifier = Modifier
                            .padding(20.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.cross),
                            contentDescription = stringResource(id = R.string.cross)
                        )
                        if (turn == 1) {
                            Text(
                                text = stringResource(id = R.string.player_one) + stringResource(id = R.string.turn),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 20.dp)
                            )
                        } else if (turn == 2) {
                            Text(
                                text = stringResource(id = R.string.player_one),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 20.dp)
                            )
                        }

                    }
                }
                Card(
                    elevation = 3.dp,
                    shape = RoundedCornerShape(16.dp),
                    backgroundColor = if (turn == 2) MaterialTheme.colors.secondary else MaterialTheme.colors.surface,
                    modifier = Modifier
                        .height(150.dp)
                        .width(120.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top,
                        modifier = Modifier
                            .padding(20.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.circle),
                            contentDescription = stringResource(id = R.string.circle)
                        )
                        if (turn == 1) {
                            Text(
                                text = stringResource(id = R.string.player_two),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 20.dp)
                            )
                        } else if (turn == 2) {
                            Text(
                                text = stringResource(id = R.string.player_two) + stringResource(id = R.string.turn),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 20.dp)
                            )
                        }

                    }
                }
            }

            GameBoard(turn) {
                turn = if (turn == 1)
                    2
                else
                    1
            }

            if (Build.VERSION.SDK_INT >= 31) {
                Button(
                    interactionSource = MutableInteractionSource(),
                    onClick = onNavigateToMenu, modifier = Modifier.wrapContentSize()
                ) {
                    Text(stringResource(id = R.string.back))
                }
            }
        }
    }
}

@Composable
fun GameBoard(turn: Int, onClick: () -> Unit) {

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        userScrollEnabled = false,
        modifier = Modifier.border(
            4.dp, MaterialTheme.colors.secondary
        )
    ) {
        items(3) { row ->
            LazyRow(
                userScrollEnabled = false,
                horizontalArrangement = Arrangement.Center
            ) {
                items(3) { item ->
                    GameBoardButton(turn) { onClick() }
                }
            }
        }
    }
}

/*
@Composable
fun GameBoard2(){
    val viewModel = GameBoardViewModel()

    LazyHorizontalGrid(rows = GridCells.Fixed(3)){
        itemsIndexed(viewModel.buttons2){ index, item ->
            GameBoardButton2(item) {}
        }
    }
}

@Composable
fun GameBoardButton2(someData: Any, onButtonUpdate: () -> Unit) {
    var iconLocal by remember {
        mutableStateOf(R.drawable.ic_nothing)
    }
    var enabled by rememberSaveable {
        mutableStateOf(true)
    }
    var tint by remember {
        mutableStateOf(CrossRed)
    }
    var contentDescription by remember {
        mutableStateOf("")
    }
    /*val openDialog = remember { mutableStateOf(false) }
    if (openDialog.value)
        RematchDialog(openDialog)*/

    IconButton(
        interactionSource = MutableInteractionSource(),
        onClick = {
            onButtonUpdate(item.copy(dr))
        },
        modifier = Modifier
            .size(100.dp)
            .border(
                2.dp, MaterialTheme.colors.secondary
            )
    ) {
        Icon(
            painter = painterResource(id = iconLocal),
            tint = tint,
            contentDescription = contentDescription
        )
    }
}
*/
@Composable
fun GameBoardButton(turn: Int, onClick: () -> Unit) {
    var iconLocal by remember {
        mutableStateOf(R.drawable.ic_nothing)
    }
    var enabled by rememberSaveable {
        mutableStateOf(true)
    }
    var tint by remember {
        mutableStateOf(CrossRed)
    }
    var contentDescription by remember {
        mutableStateOf("")
    }

    IconButton(
        interactionSource = MutableInteractionSource(),
        onClick = {
            if (enabled) {
                iconLocal = if (turn == 1) R.drawable.cross else R.drawable.circle
                tint =
                    if (iconLocal == R.drawable.cross) CrossRed else CircleBlue
                //contentDescription = if(iconLocal == R.drawable.cross) stringResource(id = R.string.cross) else stringResource(id = R.string.circle)
                enabled = false
                onClick()
            }
        },
        modifier = Modifier
            .size(100.dp)
            .border(
                2.dp, MaterialTheme.colors.secondary
            )
    ) {
        Icon(
            painter = painterResource(id = iconLocal),
            tint = tint,
            contentDescription = contentDescription
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GameScreenPreviewDark() {
    TicTacToeTheme(darkTheme = true) {
        GameScreen {}
    }
}

@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    TicTacToeTheme {
        GameScreen {}
    }
}