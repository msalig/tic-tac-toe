package net.salig.tictactoe.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import net.salig.tictactoe.core.theme.TicTacToeTheme
import net.salig.tictactoe.provider.TicTacToeNavGraph

class TicTacToeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTacToeTheme {
                TicTacToeNavGraph()
            }
        }
    }
}