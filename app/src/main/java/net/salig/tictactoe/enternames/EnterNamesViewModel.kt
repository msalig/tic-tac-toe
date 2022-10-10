package net.salig.tictactoe.enternames

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import net.salig.tictactoe.R

class EnterNamesViewModel() : ViewModel() {
    var name: String by mutableStateOf("")
    var isError by mutableStateOf(false)
    var name2: String by mutableStateOf("")
    var isError2 by mutableStateOf(false)

    var errorMessage by mutableStateOf(R.string.nothing)
    var errorMessage2 by mutableStateOf(R.string.nothing)


    fun validate(text: String, textField: Int): Boolean {
        if (text.length > 15) {
            if (textField == 1)
                errorMessage = R.string.error_message_length
            else
                errorMessage2 = R.string.error_message_length
            return true
        } else if (!text.matches(Regex("^[a-zA-Z0-9. ]*\$"))) {
            if (textField == 1)
                errorMessage = R.string.error_message_symbols
            else
                errorMessage2 = R.string.error_message_symbols
            return true
        }

        return !(text.matches(Regex("^[a-zA-Z0-9. ]*\$")) && text.length < 15)
    }
}