package net.salig.tictactoe.data.model

data class GameState(
    val selfPlayerName: String = "",
    val otherPlayerName: String = "",
    val selfPlayerHighscore: Int = 0,
    val otherPlayerHighscore: Int = 0,
    val playerAtTurn: String = "",
    val field: Array<Array<Char?>> = emptyField(),
    val winningPlayer: String? = null,
    val isBoardFull: Boolean = false,
) {

    companion object {
        fun emptyField(): Array<Array<Char?>> {
            return Array(3) { Array(3) { null } }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
/*        if (javaClass != other?.javaClass) return false*/

        other as GameState

/*        if (selfPlayerName != other.selfPlayerName) return false
        if (otherPlayerName != other.otherPlayerName) return false
        if (playerAtTurn != other.playerAtTurn) return false*/
        if (!field.contentDeepEquals(other.field)) return false
/*        if (winningPlayer != other.winningPlayer) return false
        if (isBoardFull != other.isBoardFull) return false*/

        return super.equals(other)
    }

    override fun hashCode(): Int {
        var result = selfPlayerName.hashCode()
        result = 31 * result + otherPlayerName.hashCode()
        result = 31 * result + playerAtTurn.hashCode()
        result = 31 * result + field.contentDeepHashCode()
        result = 31 * result + winningPlayer.hashCode()
        result = 31 * result + isBoardFull.hashCode()
        return result
    }
}