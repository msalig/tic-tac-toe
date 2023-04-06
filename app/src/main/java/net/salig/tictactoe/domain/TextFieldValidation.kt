package net.salig.tictactoe.domain

import net.salig.tictactoe.R
import net.salig.tictactoe.core.Constants

fun validateTextFieldInput(playerName: String): Int? = when {
    (playerName.length > Constants.MAX_TEXT_FIELD_CHARACTERS) -> R.string.error_message_length

    !playerName.matches(Regex("^[a-zA-Z0-9. ]*\$")) -> R.string.error_message_symbols

    else -> null
}