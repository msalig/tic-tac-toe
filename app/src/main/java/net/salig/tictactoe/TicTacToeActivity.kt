package net.salig.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import net.salig.tictactoe.ui.theme.TicTacToeTheme

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