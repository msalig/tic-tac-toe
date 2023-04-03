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
import net.salig.tictactoe.presentation.gamemode.GamemodeScreen
import net.salig.tictactoe.presentation.menu.MenuScreen

@Composable
fun TicTacToeNavGraph(
    activity: ViewModelStoreOwner,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = TicTacToeDestinations.MENU_ROUTE,
    navActions: TicTacToeNavigationActions = remember(navController) {
        TicTacToeNavigationActions(navController)
    },
) {

    val viewModel = viewModel<GameScreenViewModel>(viewModelStoreOwner = activity)

    NavHost(navController = navController,
        startDestination = startDestination,
        modifier = modifier) {

        composable(TicTacToeDestinations.MENU_ROUTE) {
            MenuScreen { navActions.navigateToGamemode() }
        }

        composable(TicTacToeDestinations.GAMEMODE_ROUTE) {

            GamemodeScreen(onNavigateToEnterNamesScreen = { navActions.navigateToEnterNames() },
                onNavigateToGameScreen = { navActions.navigateToGame() },
                viewModel = viewModel)
        }

        composable(TicTacToeDestinations.ENTER_NAMES_ROUTE) {
            EnterNamesScreen(onNavigateToGameScreen = { navActions.navigateToGame() },
                viewModel = viewModel)
        }

        composable(TicTacToeDestinations.GAME_ROUTE) {
            GameScreen(onNavigateToMenu = { navActions.navigateToMenu() }, viewModel = viewModel)
        }
    }
}