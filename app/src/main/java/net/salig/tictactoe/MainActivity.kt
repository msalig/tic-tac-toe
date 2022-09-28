package net.salig.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import net.salig.tictactoe.ui.theme.TicTacToeTheme
import net.salig.tictactoe.ui.theme.Typography

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTacToeTheme {
                TicTacToeNavHost()
            }
        }
    }
}

@Composable
fun TicTacToeNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "menu"
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable("menu") { TicTacToeApp(onNavigateToGameBoard = { navController.navigate("gameBoard") }) }
        composable("gameBoard") { GameScreen(onNavigateToMenu = { navController.navigate("menu") }) }
    }
}

@Composable
fun TicTacToeApp(onNavigateToGameBoard: () -> Unit) {
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
                stringResource(id = R.string.app_name), modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize()
                    .padding(top = 20.dp), style = Typography.h2
            )

            Image(
                painterResource(id = R.drawable.image),
                contentDescription = null,
                modifier = Modifier
                    .size(
                        LocalConfiguration.current.screenWidthDp.dp
                    )
                    .padding(40.dp)
            )

            Button(
                interactionSource = MutableInteractionSource(),
                onClick = onNavigateToGameBoard
            ) {
                Text(stringResource(id = R.string.start))
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TicTacToeApp {}
}
