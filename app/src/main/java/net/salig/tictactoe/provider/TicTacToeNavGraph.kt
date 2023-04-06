package net.salig.tictactoe.provider

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import net.salig.tictactoe.presentation.enternames.EnterNamesScreen
import net.salig.tictactoe.presentation.game.GameScreen
import net.salig.tictactoe.presentation.game.GameScreenViewModel
import net.salig.tictactoe.presentation.gamemode.GameModeScreen
import net.salig.tictactoe.presentation.menu.MenuScreen
import net.salig.tictactoe.provider.TicTacToeNavigationActions.Companion.ENTER_NAMES_SCREEN
import net.salig.tictactoe.provider.TicTacToeNavigationActions.Companion.GAME_MODE_SCREEN
import net.salig.tictactoe.provider.TicTacToeNavigationActions.Companion.GAME_SCREEN
import net.salig.tictactoe.provider.TicTacToeNavigationActions.Companion.MENU_SCREEN

@Composable
fun TicTacToeNavGraph(
    activity: ViewModelStoreOwner,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = MENU_SCREEN,
    navActions: TicTacToeNavigationActions = remember(navController) {
        TicTacToeNavigationActions(navController)
    },
) {

    val viewModel = viewModel<GameScreenViewModel>(viewModelStoreOwner = activity)

    NavHost(navController = navController,
        startDestination = startDestination,
        modifier = modifier) {

        composable(MENU_SCREEN) {
            MenuScreen { navActions.navigateToGameMode() }
        }

        composable(GAME_MODE_SCREEN) {

            GameModeScreen(onNavigateToEnterNamesScreen = { navActions.navigateToEnterNames() },
                onNavigateToGameScreen = { navActions.navigateToGame() },
                viewModel = viewModel)
        }

        composable(ENTER_NAMES_SCREEN) {
            EnterNamesScreen(onNavigateToGameScreen = { navActions.navigateToGame() },
                viewModel = viewModel)
        }

        composable(GAME_SCREEN) {
            GameScreen(onNavigateToMenu = { navActions.navigateToMenu() }, viewModel = viewModel)
        }
    }
}