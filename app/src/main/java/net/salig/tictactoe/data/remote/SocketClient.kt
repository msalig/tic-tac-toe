package net.salig.tictactoe.data.remote

import android.util.Log
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class SocketClient(
    private val messageHandler: MessageHandler,
    private val setConnected: (Boolean) -> Unit,
) {

    private var socket: Socket? = null
    private var dataInputStream: DataInputStream? = null
    private var dataOutputStream: DataOutputStream? = null

    private var executor: Executor = Executors.newFixedThreadPool(2)

    fun connectToServer(host: String, port: Int) {
        Thread {
            try {
                Log.d(TAG, "Connecting to server...")
                socket = Socket()
                socket?.keepAlive = true
                //socket?.tcpNoDelay = true
                socket?.connect(InetSocketAddress(host, port), CONNECTION_TIMEOUT)

                if (socket?.isConnected == true) {
                    Log.d(TAG, "Connected")
                    setConnected(true)

                    dataInputStream = DataInputStream(socket?.getInputStream())
                    dataOutputStream = DataOutputStream(socket?.getOutputStream())

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
        private const val CONNECTION_TIMEOUT = 30000
        private val TAG = SocketClient::class.java.simpleName
    }
}
