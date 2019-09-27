package com.konsung.basic.net

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.konsung.basic.util.Debug


/**
 * 网络状态
 */
class NetworkStatusCallback private constructor() : ConnectivityManager.NetworkCallback() {

    companion object {

        val TAG: String = NetworkStatusCallback::class.java.simpleName

        var mType = NetworkType.NETWORK_NO

        private var networkCallback: NetworkStatusCallback? = null

        private val mObservers = mutableListOf<NetStateChangeObserver>()

        fun registerNetwork(context: Context) {
            val request = NetworkRequest.Builder().build()
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

            unRegisterAll()
            networkCallback = NetworkStatusCallback()
            cm?.registerNetworkCallback(request, networkCallback)
        }

        fun unRegisterNetwork(context: Context) {

            unRegisterAll()
            networkCallback?.let {
                val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
                cm?.unregisterNetworkCallback(it)
            }
        }

        fun registerObserver(observer: NetStateChangeObserver?) {
            if (observer == null) {
                return
            }
            if (!mObservers.contains(observer)) {
                mObservers.add(observer)
            }
        }

        fun unRegisterObserver(observer: NetStateChangeObserver?) {

            if (observer == null) {
                return
            }

            mObservers.remove(observer)
        }

        private fun unRegisterAll() {
            for (observer in mObservers) {
                unRegisterObserver(observer)
            }
            mObservers.clear()
        }

        private fun notifyObservers(networkType: NetworkType) {

            if (mType === networkType) {
                return
            }

            mType = networkType
            if (networkType === NetworkType.NETWORK_NO) {
                for (observer in mObservers) {
                    observer.onNetDisconnected()
                }
            } else {
                for (observer in mObservers) {
                    observer.onNetConnected(networkType)
                }
            }
        }
    }

    init {
        Debug.info(TAG, "mType=$mType ")
    }

    override fun onAvailable(network: Network?) {
        super.onAvailable(network)
        Debug.info(TAG, "onAvailable ")
        notifyObservers(NetworkType.NETWORK_UNKNOWN)
    }

    override fun onLost(network: Network?) {
        super.onLost(network)
        Debug.info(TAG, "onLost ")
        notifyObservers(NetworkType.NETWORK_NO)
    }

    override fun onCapabilitiesChanged(network: Network?, networkCapabilities: NetworkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities)

        Debug.info(TAG, "onCapabilitiesChanged networkCapabilities=$networkCapabilities")

        if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {

            when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> notifyObservers(NetworkType.NETWORK_WIFI)

                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> notifyObservers(NetworkType.NETWORK_4G)

                else -> notifyObservers(NetworkType.NETWORK_UNKNOWN)
            }
        }
    }
}