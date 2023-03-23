package net.salig.tictactoe.provider

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import net.salig.tictactoe.R
import net.salig.tictactoe.presentation.enternames.EnterNamesScreen
import net.salig.tictactoe.presentation.game.GameScreen
import net.salig.tictactoe.presentation.game.GameScreenViewModel
import net.salig.tictactoe.presentation.gamemode.GamemodeScreen
import net.salig.tictactoe.presentation.menu.MenuScreen
import net.salig.tictactoe.provider.TicTacToeDestinationsArgs.PLAYER_NAME_ONE_ARG
import net.salig.tictactoe.provider.TicTacToeDestinationsArgs.PLAYER_NAME_TWO_ARG

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
    val viewModelStoreOwner = checkNotNull(activity) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }

    val viewModel = viewModel<GameScreenViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    NavHost(navController = navController,
        startDestination = startDestination,
        modifier = modifier) {

        composable(TicTacToeDestinations.MENU_ROUTE) {
            MenuScreen { navActions.navigateToGamemode() }
        }

        composable(TicTacToeDestinations.GAMEMODE_ROUTE) {

            GamemodeScreen(onNavigateToEnterNamesScreen = { navActions.navigateToEnterNames() },
                onNavigateToGameScreen = { playerNameOne, playerNameTwo ->
                    navActions.navigateToGame(playerNameOne, playerNameTwo)
                },
                viewModel = viewModel)
        }

        composable(TicTacToeDestinations.ENTER_NAMES_ROUTE) {
            EnterNamesScreen(onNavigateToGameScreen = { playerNameOne, playerNameTwo ->
                navActions.navigateToGame(playerNameOne, playerNameTwo)
            })
        }

        composable(TicTacToeDestinations.GAME_ROUTE,
            arguments = listOf(navArgument(PLAYER_NAME_ONE_ARG) {
                type = NavType.StringType
            }, navArgument(PLAYER_NAME_TWO_ARG) {
                type = NavType.StringType
            })) { backStackEntry ->

            GameScreen(playerNameOne = backStackEntry.arguments?.getString(PLAYER_NAME_ONE_ARG)
                ?: stringResource(id = R.string.player_one),
                playerNameTwo = backStackEntry.arguments?.getString(PLAYER_NAME_TWO_ARG)
                    ?: stringResource(id = R.string.player_two),
                onNavigateToMenu = { navActions.navigateToMenu() },
                viewModel = viewModel)
        }
    }
}