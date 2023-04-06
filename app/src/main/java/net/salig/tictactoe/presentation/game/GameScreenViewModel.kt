package net.salig.tictactoe.presentation.game

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.salig.tictactoe.data.model.GameState
import net.salig.tictactoe.data.remote.*
import net.salig.tictactoe.data.serialization.JsonSerializer
import net.salig.tictactoe.data.serialization.model.Coordinates
import net.salig.tictactoe.data.serialization.model.Username
import net.salig.tictactoe.domain.checkIfSomeoneWon

class GameScreenViewModel : ViewModel() {
    private var nsdDiscover: NSDDiscover? = null
    private var nsdAdvertise: NSDAdvertise? = null
    private var socketClient: SocketClient? = null
    private var socketServer: SocketServer? = null
    private var messageHandler = MessageHandler(returnReceivedData = { decodeReceivedData(it) },
        setConnected = { isConnected = it })

    var isLocalNetworkMultiplayer by mutableStateOf(false)
    var isJoin by mutableStateOf(false)
    var isHost by mutableStateOf(false)
    var isConnected by mutableStateOf(false)
    var isWaitingForRematchResponse by mutableStateOf(false)
    private var receivedRematchResponse by mutableStateOf(false)

    private var buttonsAlreadyClicked = 0
    var startRematchDialogWithDelay by mutableStateOf(false)
    var showRematchDialog by mutableStateOf(false)
    var gameState by mutableStateOf(GameState())

    fun finishTurn(coordinates: Coordinates) {
        val newField = gameState.field.copyOf()
        newField[coordinates.y][coordinates.x] =
            if ((isLocalNetworkMultiplayer && isHost) || (!isLocalNetworkMultiplayer && gameState.playerAtTurn == gameState.selfPlayerName)) 'X' else 'O'

        if (isLocalNetworkMultiplayer) {
            messageHandler.sendData(Json.encodeToString(coordinates))

            receivedRematchResponse = false
        }

        updateGameState(newField)

        checkForWinner(coordinates)
    }

    private fun updateGameState(field: Array<Array<Char?>>) {
        gameState =
            gameState.copy(playerAtTurn = if (gameState.playerAtTurn == gameState.selfPlayerName) {
                gameState.otherPlayerName
            } else {
                gameState.selfPlayerName
            }, field = field)
    }

    private fun checkForWinner(coordinates: Coordinates) {
        if (buttonsAlreadyClicked >= 4) {
            checkIfSomeoneWon(gameState.field, coordinates, setWon = { won ->
                startRematchDialogWithDelay = true
                gameState = when (won) {
                    true -> {
                        if (gameState.playerAtTurn != gameState.selfPlayerName) {
                            gameState.copy(winningPlayer = gameState.selfPlayerName,
                                selfPlayerHighscore = gameState.selfPlayerHighscore + 1)
                        } else {
                            gameState.copy(winningPlayer = gameState.otherPlayerName,
                                otherPlayerHighscore = gameState.otherPlayerHighscore + 1)
                        }
                    }
                    false -> gameState.copy(isBoardFull = true)
                }
            })
        } else {
            buttonsAlreadyClicked++
        }
    }

    fun rematch() {
        gameState = gameState.copy(field = GameState.emptyField(),
            winningPlayer = null,
            isBoardFull = false)

        buttonsAlreadyClicked = 0

        if (isLocalNetworkMultiplayer) {
            messageHandler.sendData(Json.encodeToString(Username(gameState.selfPlayerName)))
            if (!receivedRematchResponse) {
                isWaitingForRematchResponse = true
            }
        }
    }

    fun hostGame(context: Context) {
        isHost = true
        var port = -1
        socketServer = SocketServer(messageHandler = messageHandler,
            setSelectedPort = { port = it },
            setConnected = { isConnected = it })

        nsdAdvertise = NSDAdvertise(context, socketServer!!, port)
    }

    fun joinGame(context: Context) {
        isJoin = true

        socketClient = SocketClient(messageHandler, setConnected = { isConnected = it })
        nsdDiscover = NSDDiscover(context, socketClient!!)
    }

    fun exchangeUsernames() {
        messageHandler.sendData(Json.encodeToString(Username(gameState.selfPlayerName)))
    }

    private fun decodeReceivedData(receivedData: String) {
        try {
            val element = Json.decodeFromString(JsonSerializer(), receivedData)
            if (element is Username) {
                if (element.name == gameState.selfPlayerName && isHost) {
                    gameState =
                        gameState.copy(selfPlayerName = gameState.selfPlayerName.plus(" Host"))
                    exchangeUsernames()
                }

                gameState = gameState.copy(otherPlayerName = element.name)

                if (gameState.otherPlayerHighscore == 0 && gameState.selfPlayerHighscore == 0) {
                    gameState =
                        gameState.copy(playerAtTurn = if (isHost) gameState.selfPlayerName else gameState.otherPlayerName)
                } else {
                    isWaitingForRematchResponse = false
                    receivedRematchResponse = true
                }
            } else {
                val coordinates = element as Coordinates
                val newField = gameState.field.copyOf()
                newField[coordinates.y][coordinates.x] = if (!isHost) 'X' else 'O'

                updateGameState(newField)
                checkForWinner(coordinates)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun shutdown() {
        stopHosting()
        stopJoining()

        isLocalNetworkMultiplayer = false
        isWaitingForRematchResponse = false
        receivedRematchResponse = false

        gameState = GameState()
    }

    fun stopHosting() {
        nsdAdvertise?.shutdown()
        isHost = false
    }

    fun stopJoining() {
        nsdDiscover?.shutdown()
        isJoin = false
    }

    override fun onCleared() {
        super.onCleared()
        shutdown()
    }
}