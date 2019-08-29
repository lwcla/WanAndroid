package com.konsung.basic.net

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.konsung.basic.util.AppUtils

class NetChangeReceiver : BroadcastReceiver() {

    companion object {
        var mType = NetworkUtil.getNetworkType(AppUtils.getContext())
        private val mObservers = mutableListOf<NetStateChangeObserver>()

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

    override fun onReceive(context: Context?, intent: Intent?) {
        val networkType = NetworkUtil.getNetworkType(context)
        notifyObservers(networkType)
    }

}