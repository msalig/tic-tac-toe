package net.salig.tictactoe.data.remote

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.util.Log
import android.widget.Toast
import net.salig.tictactoe.core.Constants

class NSDDiscover(
    private val context: Context,
    private val client: SocketClient,
) {
    private val nsdManager: NsdManager = context.getSystemService(Context.NSD_SERVICE) as NsdManager
    private var host: String? = null
    private var port = 0
    private var currentDiscoveryStatus = DISCOVERY_STATUS.OFF

    private enum class DISCOVERY_STATUS { ON, OFF }

    fun discoverServices() {
        if (currentDiscoveryStatus == DISCOVERY_STATUS.ON) return
        Toast.makeText(context, "Discover services!", Toast.LENGTH_LONG).show()
        currentDiscoveryStatus = DISCOVERY_STATUS.ON
        nsdManager.discoverServices(
            Constants.SERVICE_TYPE,
            NsdManager.PROTOCOL_DNS_SD,
            discoveryListener
        )
    }

    private var resolveListener: NsdManager.ResolveListener = object : NsdManager.ResolveListener {
        override fun onResolveFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
            Log.e(TAG, "Resolve failed$errorCode")
        }

        override fun onServiceResolved(serviceInfo: NsdServiceInfo) {
            Log.e(TAG, "Resolve Succeeded. $serviceInfo")

            Toast.makeText(context, "Found a connection!", Toast.LENGTH_LONG).show()
            nsdManager.stopServiceDiscovery(discoveryListener)
            setHostAndPortValues(serviceInfo)

            client.connectToServer(host!!, port)
        }
    }

    private fun setHostAndPortValues(serviceInfo: NsdServiceInfo) {
        host = serviceInfo.host.hostAddress
        port = serviceInfo.port
    }

    private val discoveryListener: NsdManager.DiscoveryListener =
        object : NsdManager.DiscoveryListener {
            override fun onDiscoveryStarted(regType: String) {
                Log.d(TAG, "Service discovery started")
            }

            override fun onServiceFound(service: NsdServiceInfo) {
                Log.d(TAG, "Service discovery success $service")

                if (service.serviceType != Constants.SERVICE_TYPE) {
                    Log.d(TAG, "Unknown Service Type: " + service.serviceType)
                } else {
                    nsdManager.resolveService(service, resolveListener)
                }
            }

            override fun onServiceLost(service: NsdServiceInfo) {
                Log.e(TAG, "service lost$service")
            }

            override fun onDiscoveryStopped(serviceType: String) {
                Log.i(TAG, "Discovery stopped: $serviceType")
            }

            override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {
                Log.e(TAG, "Discovery failed: Error code:$errorCode")
                nsdManager.stopServiceDiscovery(this)
            }

            override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {
                Log.e(TAG, "Discovery failed: Error code:$errorCode")
                currentDiscoveryStatus = DISCOVERY_STATUS.OFF
                nsdManager.stopServiceDiscovery(this)
            }
        }

    fun shutdown() {
        try {
            //TODO: Do not attempt stop listener when it isn't even registered
            nsdManager.stopServiceDiscovery(discoveryListener)
            client.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private val TAG: String = NSDDiscover::class.java.simpleName
    }
}