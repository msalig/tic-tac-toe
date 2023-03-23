package net.salig.tictactoe.provider

import androidx.navigation.NavHostController
import net.salig.tictactoe.provider.TicTacToeDestinations.ENTER_NAMES_ROUTE
import net.salig.tictactoe.provider.TicTacToeDestinations.MENU_ROUTE
import net.salig.tictactoe.provider.TicTacToeDestinationsArgs.PLAYER_NAME_ONE_ARG
import net.salig.tictactoe.provider.TicTacToeDestinationsArgs.PLAYER_NAME_TWO_ARG
import net.salig.tictactoe.provider.TicTacToeScreens.ENTER_NAMES_SCREEN
import net.salig.tictactoe.provider.TicTacToeScreens.GAMEMODE_SCREEN
import net.salig.tictactoe.provider.TicTacToeScreens.GAME_SCREEN
import net.salig.tictactoe.provider.TicTacToeScreens.MENU_SCREEN

/**
 * Screens used in [TicTacToeDestinations]
 */
private object TicTacToeScreens {
    const val MENU_SCREEN = "menu"
    const val GAMEMODE_SCREEN = "gamemode"
    const val ENTER_NAMES_SCREEN = "names"
    const val GAME_SCREEN = "game"
}

/**
 * Arguments used in [TicTacToeDestinations] routes
 */
object TicTacToeDestinationsArgs {
    const val PLAYER_NAME_ONE_ARG = "playerNameOne"
    const val PLAYER_NAME_TWO_ARG = "playerNameTwo"
}

/**
 * Destinations used in the [net.salig.tictactoe.presentation.TicTacToeActivity]
 */
object TicTacToeDestinations {
    const val MENU_ROUTE = MENU_SCREEN
    const val GAMEMODE_ROUTE = GAMEMODE_SCREEN
    const val ENTER_NAMES_ROUTE = ENTER_NAMES_SCREEN
    const val GAME_ROUTE =
        "$GAME_SCREEN?$PLAYER_NAME_ONE_ARG={$PLAYER_NAME_ONE_ARG}?$PLAYER_NAME_TWO_ARG={$PLAYER_NAME_TWO_ARG}"
}


/**
 * Models the navigation actions in the app.
 */
class TicTacToeNavigationActions(private val navController: NavHostController) {

    fun navigateToMenu() {
        navController.navigate(MENU_ROUTE)
    }

    fun navigateToGamemode() {
        navController.navigate(GAMEMODE_SCREEN) {
            launchSingleTop = true
        }
    }

    fun navigateToEnterNames() {
        navController.navigate(ENTER_NAMES_ROUTE) {
            launchSingleTop = true
        }
    }

    fun navigateToGame(playerNameOne: String, playerNameTwo: String) {
        navController.navigate(GAME_SCREEN.let {
            "$it?$PLAYER_NAME_ONE_ARG=$playerNameOne?$PLAYER_NAME_TWO_ARG=$playerNameTwo"
        }) {
            //popUpTo(navController.graph.findStartDestination().id)
            launchSingleTop = true
        }
    }
}