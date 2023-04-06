package net.salig.tictactoe.data.remote

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdManager.RegistrationListener
import android.net.nsd.NsdServiceInfo
import android.util.Log
import net.salig.tictactoe.core.Constants

class NSDAdvertise(
    context: Context,
    private val socketServer: SocketServer,
    private val selectedPort: Int = -1,
) {
    private val nsdManager: NsdManager = context.getSystemService(Context.NSD_SERVICE) as NsdManager
    var discoveryServiceName = "TicTacToeService"
    private var currentRegistrationStatus = REGISTRATION_STATUS.NON_REGISTERED

    private enum class REGISTRATION_STATUS {
        REGISTERED, NON_REGISTERED
    }

    /**
     * Registration Listener for our NDS Listen logic
     */
    private val registrationListener: RegistrationListener = object : RegistrationListener {
        override fun onServiceRegistered(NsdServiceInfo: NsdServiceInfo) {
            discoveryServiceName = NsdServiceInfo.serviceName
            Log.e(TAG,
                "This device has been registered to be discovered through NSD...:$discoveryServiceName")
        }

        override fun onRegistrationFailed(arg0: NsdServiceInfo, arg1: Int) {}
        override fun onServiceUnregistered(arg0: NsdServiceInfo) {}
        override fun onUnregistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {}
    }

    init {
        //Start a thread with the server socket ready to receive connections...
        socketServer.startServer()

        registerDevice()
    }

    /**
     * This method should be triggered after createServerThread has been executed...
     */
    private fun registerDevice() {
        if (currentRegistrationStatus == REGISTRATION_STATUS.REGISTERED) return
        if (selectedPort > -1) {
            registerService()
        } else {
            Log.d(TAG,
                "No Socket available..., make sure this method is called after createServerThread has been executed...")
        }
    }

    private fun registerService() {
        val serviceInfo = NsdServiceInfo()
        serviceInfo.port = selectedPort
        serviceInfo.serviceName = discoveryServiceName
        serviceInfo.serviceType = Constants.SERVICE_TYPE
        serviceInfo.setAttribute("test", "hey")
        currentRegistrationStatus = REGISTRATION_STATUS.REGISTERED
        nsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, registrationListener)
    }

    fun shutdown() {
        try {
            socketServer.close()
            if (currentRegistrationStatus == REGISTRATION_STATUS.REGISTERED) {
                nsdManager.unregisterService(registrationListener)
            }
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
        }
    }

    companion object {
        private val TAG = NSDAdvertise::class.java.simpleName
    }
}