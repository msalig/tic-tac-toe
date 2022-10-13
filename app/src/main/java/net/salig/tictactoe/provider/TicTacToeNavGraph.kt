package net.salig.tictactoe.provider

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import net.salig.tictactoe.presentation.enternames.EnterNamesScreen
import net.salig.tictactoe.presentation.game.GameScreen
import net.salig.tictactoe.presentation.menu.MenuScreen
import net.salig.tictactoe.provider.TicTacToeDestinationsArgs.PLAYER_NAME_ONE_ARG
import net.salig.tictactoe.provider.TicTacToeDestinationsArgs.PLAYER_NAME_TWO_ARG

@Composable
fun TicTacToeNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = TicTacToeDestinations.MENU_ROUTE,
    navActions: TodoNavigationActions = remember(navController) {
        TodoNavigationActions(navController)
    }
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(TicTacToeDestinations.MENU_ROUTE) { MenuScreen(onNavigateToEnterNameScreen = { navActions.navigateToEnterNames() }) }
        composable(TicTacToeDestinations.ENTER_NAMES_ROUTE) {
            EnterNamesScreen(
                onNavigateToGameScreen = { playerNameOne, playerNameTwo ->
                    navActions.navigateToGame(
                        playerNameOne,
                        playerNameTwo
                    )
                })
        }
        composable(
            TicTacToeDestinations.GAME_ROUTE,
            arguments = listOf(
                navArgument(PLAYER_NAME_ONE_ARG) { type = NavType.StringType },
                navArgument(PLAYER_NAME_TWO_ARG) { type = NavType.StringType })
        ) { entry ->
            GameScreen(playerNameOne = entry.arguments?.getString(PLAYER_NAME_ONE_ARG),
                playerNameTwo = entry.arguments?.getString(
                    PLAYER_NAME_TWO_ARG
                ),
                onNavigateToMenu = { navActions.navigateToMenu() })
        }
    }
}