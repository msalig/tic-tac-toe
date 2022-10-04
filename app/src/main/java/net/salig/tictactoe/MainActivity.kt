package net.salig.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import net.salig.tictactoe.ui.theme.TicTacToeTheme

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
        composable("menu") { TicTacToeApp(onNavigateToGameBoard = { navController.navigate("gameScreen") }) }
        composable("gameScreen") { GameScreen(onNavigateToMenu = { navController.navigate("menu") }) }
    }
}

@Composable
fun TicTacToeApp(onNavigateToGameBoard: () -> Unit) {
    StartScreen { onNavigateToGameBoard() }
}