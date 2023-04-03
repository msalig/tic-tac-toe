package net.salig.tictactoe.data.remote

import android.util.Log
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.net.Socket
import java.net.SocketTimeoutException

class MessageHandler(
    private val returnReceivedData: (String) -> Unit,
    private val setConnected: (Boolean) -> Unit,
) {

    private var socket: Socket? = null
    private var dataInputStream: DataInputStream? = null
    private var dataOutputStream: DataOutputStream? = null

    fun init(socket: Socket?) {
        this.socket = socket
        try {
            dataInputStream = DataInputStream(socket?.getInputStream())
            dataOutputStream = DataOutputStream(socket?.getOutputStream())
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun sendData(input: String) {
        Thread {
            if (isConnected) {
                try {
                    dataOutputStream?.writeUTF(input)
                    dataOutputStream?.flush()
                    Log.d(TAG, "sent: \"$input\"")
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }.start()
    }

    fun receiveData() {
        while (isConnected) {
            try {
                val input = dataInputStream?.readUTF()

                if (input != null && input.isNotEmpty()) {
                    returnReceivedData(input)
                }

                Log.d(TAG, "received: \"$input\"")
            } catch (e: SocketTimeoutException) {
                e.printStackTrace()
                break
            } catch (e: IOException) {
                e.printStackTrace()
                break
            }
        }

        Log.d(TAG, "Disconnected")
        setConnected(false)
    }

    private val isConnected: Boolean
        get() = socket != null && socket?.isConnected == true && socket?.isClosed == false

    companion object {
        private var TAG = MessageHandler::class.java.simpleName
    }
}
