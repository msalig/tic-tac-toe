package net.salig.tictactoe

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import net.salig.tictactoe.TicTacToeDestinations.ENTER_NAMES_ROUTE
import net.salig.tictactoe.TicTacToeDestinations.MENU_ROUTE
import net.salig.tictactoe.TicTacToeDestinationsArgs.PLAYER_NAME_ONE_ARG
import net.salig.tictactoe.TicTacToeDestinationsArgs.PLAYER_NAME_TWO_ARG
import net.salig.tictactoe.TicTacToeScreens.ENTER_NAMES_SCREEN
import net.salig.tictactoe.TicTacToeScreens.GAME_SCREEN
import net.salig.tictactoe.TicTacToeScreens.MENU_SCREEN

/**
 * Screens used in [TicTacToeDestinations]
 */
private object TicTacToeScreens {
    const val MENU_SCREEN = "menu"
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
 * Destinations used in the [TicTacToeActivity]
 */
object TicTacToeDestinations {
    const val MENU_ROUTE = MENU_SCREEN
    const val ENTER_NAMES_ROUTE = ENTER_NAMES_SCREEN
    const val GAME_ROUTE =
        "$GAME_SCREEN?$PLAYER_NAME_ONE_ARG={$PLAYER_NAME_ONE_ARG}?$PLAYER_NAME_TWO_ARG={$PLAYER_NAME_TWO_ARG}"
}


/**
 * Models the navigation actions in the app.
 */
class TodoNavigationActions(private val navController: NavHostController) {

    fun navigateToMenu() {
        navController.navigate(
            MENU_ROUTE
        ) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
        }
    }

    fun navigateToEnterNames() {
        navController.navigate(ENTER_NAMES_ROUTE) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }

    fun navigateToGame(playerNameOne: String?, playerNameTwo: String?) {
        navController.navigate(GAME_SCREEN.let {
            if (playerNameOne != null && playerNameTwo != null)
                "$it?$PLAYER_NAME_ONE_ARG=$playerNameOne?$PLAYER_NAME_TWO_ARG=$playerNameTwo" else it
        }) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }

            launchSingleTop = true
        }
    }
}