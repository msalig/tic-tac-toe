package net.salig.tictactoe.data.remote

import android.util.Log
import java.io.DataInputStream
import java.io.DataOutputStream
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
    private var dataInputStream: DataInputStream? = null
    private var dataOutputStream: DataOutputStream? = null

    private lateinit var serverSocket: ServerSocket

    private var executor: Executor = Executors.newFixedThreadPool(2)

    init {
        try {
            serverSocket = ServerSocket(0)
            setSelectedPort(serverSocket.localPort)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun startServer() {
        Thread {
            try {
                Log.d(TAG, "serverSocket waiting for connection to accept ...")
                socket = serverSocket.accept()

                if (socket!!.isConnected) {
                    Log.d(TAG, "Connected")
                    setConnected(true)

                    dataInputStream = DataInputStream(socket?.getInputStream())
                    dataOutputStream = DataOutputStream(socket?.getOutputStream())

                    messageHandler.init(socket)

                    executor.execute {
                        messageHandler.receiveData()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    fun close() {
        if (socket != null) {
            try {
                socket?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        private val TAG = SocketServer::class.java.simpleName
    }
}