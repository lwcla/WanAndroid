package com.konsung.basic.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.konsung.basic.config.BaseConfig

interface CollectResult {
    fun collectResult(success: Boolean, collectId: Int, position: Int, toCollect: Boolean)
}

class CollectReceiver : BroadcastReceiver() {

    companion object {
        private val mObservers = mutableListOf<CollectResult>()

        fun registerObserver(observer: CollectResult?) {
            if (observer == null) {
                return
            }
            if (!mObservers.contains(observer)) {
                mObservers.add(observer)
            }
        }

        fun unRegisterObserver(observer: CollectResult?) {

            if (observer == null) {
                return
            }

            mObservers.remove(observer)
        }

        private fun notifyCollectResult(success: Boolean, collectId: Int, position: Int, toCollect: Boolean) {
            for (observer in mObservers) {
                observer.collectResult(success, collectId, position, toCollect)
            }
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) {
            return
        }

        if (intent.action == BaseConfig.COLLECT_RESULT_ACTION) {
            //收藏结果

            val collectResult = intent.getBooleanExtra(BaseConfig.COLLECT_RESULT, false)
            val collectDataPosition = intent.getIntExtra(BaseConfig.COLLECT_DATA_POSITION, -1)
            val toCollect = intent.getBooleanExtra(BaseConfig.TO_COLLECT, false)
            val collectId = intent.getIntExtra(BaseConfig.COLLECT_ID, -1)

            if (collectDataPosition < 0 || collectId < 0) {
                return
            }

            notifyCollectResult(collectResult, collectId, collectDataPosition, toCollect)
        }
    }
}