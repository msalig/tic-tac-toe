package net.salig.tictactoe.provider

import androidx.navigation.NavHostController

class TicTacToeNavigationActions(private val navController: NavHostController) {

    fun navigateToMenu() {
        navController.navigate(MENU_SCREEN) {
            launchSingleTop = true
        }
    }

    fun navigateToGameMode() {
        navController.navigate(GAME_MODE_SCREEN) {
            launchSingleTop = true
        }
    }

    fun navigateToEnterNames() {
        navController.navigate(ENTER_NAMES_SCREEN) {
            launchSingleTop = true
        }
    }

    fun navigateToGame() {
        navController.navigate(GAME_SCREEN) {
            launchSingleTop = true
        }
    }

    companion object {
        const val MENU_SCREEN = "menu"
        const val GAME_MODE_SCREEN = "game_mode"
        const val ENTER_NAMES_SCREEN = "names"
        const val GAME_SCREEN = "game"
    }
}