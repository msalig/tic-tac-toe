package net.salig.tictactoe.core.component

import androidx.annotation.StringRes
import net.salig.tictactoe.R

data class TextFieldError(
    @StringRes val errorMessage: Int = R.string.nothing,
    val isError: Boolean = false,
)