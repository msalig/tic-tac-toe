package net.salig.tictactoe.presentation.enternames

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import net.salig.tictactoe.core.component.TextFieldError

class EnterNamesViewModel : ViewModel() {
    var name: String by mutableStateOf("")
    var error by mutableStateOf(TextFieldError())
    var name2: String by mutableStateOf("")
    var error2 by mutableStateOf(TextFieldError())
}