package net.salig.tictactoe.data.remote

import android.util.Log
import java.io.IOException
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class SocketServer(
    private val messageHandler: MessageHandler,
    setSelectedPort: (Int) -> Unit,
    private val setConnected: (Boolean) -> Unit,
) {
    private var socket: Socket? = null

    private lateinit var serverSocket: ServerSocket

    private val executor: Executor = Executors.newFixedThreadPool(2)

    init {
        try {
            serverSocket = ServerSocket(0)
            setSelectedPort(serverSocket.localPort)
        } catch (e: IOException) {
            Log.e(TAG, e.message.toString())
        }
    }

    fun startServer() {
        Thread {
            try {
                Log.d(TAG, "ServerSocket waiting for connection to accept ...")
                socket = serverSocket.accept()

                if (socket?.isConnected == true) {
                    Log.d(TAG, "Connected")
                    setConnected(true)

                    messageHandler.init(socket!!)

                    executor.execute {
                        messageHandler.receiveData()
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, e.message.toString())
            }
        }.start()
    }

    fun close() {
        if (socket != null) {
            try {
                socket?.close()
            } catch (e: IOException) {
                Log.e(TAG, e.message.toString())
            }
        }
    }

    companion object {
        private val TAG = SocketServer::class.java.simpleName
    }
}