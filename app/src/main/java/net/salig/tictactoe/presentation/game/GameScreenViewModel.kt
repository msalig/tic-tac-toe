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
    private var serverSocket: SocketServer? = null
    private var messageHandler = MessageHandler(returnReceivedData = { decodeReceivedData(it) },
        setConnected = { isConnected = it })

    var isLocalNetworkMultiplayer by mutableStateOf(false)
    var isJoin by mutableStateOf(false)
    var isHost by mutableStateOf(false)
    var isConnected by mutableStateOf(false)
    var isWaitingForRematchResponse by mutableStateOf(false)
    private var receivedRematchResponse by mutableStateOf(false)

    private var buttonsAlreadyClicked = 0
    var showRematchDialog by mutableStateOf(false)
    var showRematchDialogAfterDelay by mutableStateOf(false)
    var state by mutableStateOf(GameState())

    fun finishTurn(coordinates: Coordinates) {
        val newField = state.field.copyOf()
        newField[coordinates.y][coordinates.x] =
            if ((isLocalNetworkMultiplayer && isHost) || (!isLocalNetworkMultiplayer && state.playerAtTurn == state.selfPlayerName)) 'X' else 'O'

        if (isLocalNetworkMultiplayer) {
            messageHandler.sendData(Json.encodeToString(coordinates))

            receivedRematchResponse = false
        }

        updateState(coordinates, newField)
    }


    private fun updateState(coordinates: Coordinates, field: Array<Array<Char?>>) {
        state =
            state.copy(playerAtTurn = if (state.playerAtTurn == state.selfPlayerName) state.otherPlayerName
            else state.selfPlayerName, field = field)

        if (buttonsAlreadyClicked >= 4) {
            checkIfSomeoneWon(state.field, coordinates, setWon = {
                showRematchDialog = true
                state = when (it) {
                    true -> {
                        if (state.playerAtTurn != state.selfPlayerName) {
                            state.copy(winningPlayer = state.selfPlayerName,
                                selfPlayerHighscore = state.selfPlayerHighscore + 1)
                        } else {
                            state.copy(winningPlayer = state.otherPlayerName,
                                otherPlayerHighscore = state.otherPlayerHighscore + 1)
                        }
                    }
                    false -> state.copy(isBoardFull = true)
                }
            })
        } else {
            buttonsAlreadyClicked++
        }
    }

    fun rematch() {
        state =
            state.copy(field = GameState.emptyField(), winningPlayer = null, isBoardFull = false)

        buttonsAlreadyClicked = 0

        if (isLocalNetworkMultiplayer) {
            messageHandler.sendData(Json.encodeToString(Username(state.selfPlayerName)))
            if (!receivedRematchResponse) {
                isWaitingForRematchResponse = true
            }
        }
    }

    fun hostGame(context: Context) {
        isHost = true
        var port = -1
        serverSocket = SocketServer(messageHandler,
            setSelectedPort = { port = it },
            setConnected = { isConnected = it })

        nsdAdvertise = NSDAdvertise(context, serverSocket!!)
        nsdAdvertise?.registerDevice(port)
    }

    fun joinGame(context: Context) {
        isJoin = true

        socketClient = SocketClient(messageHandler, setConnected = { isConnected = it })

        nsdDiscover = NSDDiscover(context, socketClient!!)
        nsdDiscover?.discoverServices()
    }

    fun shutdown() {
        state = GameState()

        stopHosting()
        stopJoining()

        isLocalNetworkMultiplayer = false
    }

    fun stopHosting() {
        nsdAdvertise?.shutdown()
        isHost = false
    }

    fun stopJoining() {
        nsdDiscover?.shutdown()
        isJoin = false
    }

    fun exchangeUsernames() {
        messageHandler.sendData(Json.encodeToString(Username(state.selfPlayerName)))
    }

    private fun decodeReceivedData(receivedData: String) {
        try {
            val element = Json.decodeFromString(JsonSerializer(), receivedData)
            //TODO: Handling for the same usernames
            if (element is Username) {
                state = state.copy(otherPlayerName = element.name)

                if (state.otherPlayerHighscore == 0 && state.selfPlayerHighscore == 0) {
                    state =
                        state.copy(playerAtTurn = if (isHost) state.selfPlayerName else state.otherPlayerName)
                } else {
                    isWaitingForRematchResponse = false
                    receivedRematchResponse = true
                }
            } else {
                val coordinates = element as Coordinates
                val newField = state.field.copyOf()
                newField[coordinates.y][coordinates.x] = if (!isHost) 'X' else 'O'

                updateState(coordinates, newField)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}