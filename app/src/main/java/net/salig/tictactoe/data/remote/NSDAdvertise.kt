package net.salig.tictactoe.data.remote

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdManager.RegistrationListener
import android.net.nsd.NsdServiceInfo
import android.util.Log
import android.widget.Toast
import net.salig.tictactoe.core.Constants

class NSDAdvertise(private val context: Context, private val socketServer: SocketServer) {
    private val nsdManager: NsdManager = context.getSystemService(Context.NSD_SERVICE) as NsdManager
    var discoveryServiceName = "TicTacToeService"
    private var selectedPort = -1
    private var currentRegistrationStatus = REGISTRATION_STATUS.NON_REGISTERED

    private enum class REGISTRATION_STATUS {
        REGISTERED, NON_REGISTERED
    }

    init {
        //Start a thread with the server socket ready to receive connections...
        socketServer.startServer()
    }

    /**
     * This method should be triggered after createServerThread has been executed...
     */
    fun registerDevice(port: Int) {
        selectedPort = port

        if (currentRegistrationStatus == REGISTRATION_STATUS.REGISTERED) return
        if (selectedPort > -1) {
            setupDeviceRegistration()
        } else {
            Log.d(
                TAG,
                "No Socket available..., make sure this method is called after createServerThread has been executed..."
            )
        }
    }

    private fun setupDeviceRegistration() {
        val serviceInfo = NsdServiceInfo()
        serviceInfo.port = selectedPort
        serviceInfo.serviceName = discoveryServiceName
        serviceInfo.serviceType = Constants.SERVICE_TYPE
        currentRegistrationStatus = REGISTRATION_STATUS.REGISTERED
        nsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, registrationListener)
    }

    /**
     * Registration Listener for our NDS Listen logic
     */
    private val registrationListener: RegistrationListener = object : RegistrationListener {
        override fun onServiceRegistered(NsdServiceInfo: NsdServiceInfo) {
            discoveryServiceName = NsdServiceInfo.serviceName
            Toast.makeText(context, "Registered device!", Toast.LENGTH_LONG).show()
            Log.e(
                TAG,
                "This device has been registered to be discovered through NSD...:$discoveryServiceName"
            )
        }

        override fun onRegistrationFailed(arg0: NsdServiceInfo, arg1: Int) {}
        override fun onServiceUnregistered(arg0: NsdServiceInfo) {}
        override fun onUnregistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {}
    }

    fun shutdown() {
        try {
            nsdManager.unregisterService(registrationListener)
            socketServer.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private val TAG = NSDAdvertise::class.java.simpleName
    }
}