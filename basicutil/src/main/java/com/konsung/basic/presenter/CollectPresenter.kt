package com.konsung.basic.presenter

import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.konsung.basic.config.BaseConfig
import com.konsung.basic.config.RequestResult
import com.konsung.basic.ui.BasePresenter1
import com.konsung.basic.ui.BasicView
import com.konsung.basic.ui.UiView
import com.konsung.basic.util.AppUtils
import com.konsung.basic.util.R
import com.konsung.basic.util.toast

class CollectPresenter(uiView: UiView?, view: CollectView?) : BasePresenter1<String, CollectView>(uiView, view) {

    companion object {
        val TAG: String = CollectPresenter::class.java.simpleName
    }

    var sendMessage = false

    /**
     * 上报收藏信息
     * @param position 收藏的文章的位置
     * @param id 文章的id
     * @param collect 现在是收藏还是取消状态
     *
     * @return 是否去上报数据了
     */
    fun collect(index: Int, id: Int, collect: Boolean): Boolean {

        val ctx = getContext() ?: return false
        if (!AppUtils.instance.hasLogined(ctx)) {
            //还没有登录，打开登录界面
            ctx.toast(BaseConfig.TAG, R.string.collect_after_login)
            AppUtils.startLoginAty(ctx)
            return false
        }

        val result = object : RequestResult<String>(view) {

            override fun success() {

                val c = getContext() ?: return

                if (toCollect) {
                    c.toast(BaseConfig.TAG, R.string.collect_success)
                } else {
                    c.toast(BaseConfig.TAG, R.string.cancel_collect_success)
                }

                sendMessage(c, true, collectId, position, toCollect)

                getUiView()?.showContentView()
                view?.success(c, position, toCollect)
            }

            override fun failed(message: String) {

                val c = getContext() ?: return

                if (toCollect) {
                    c.toast(BaseConfig.TAG, R.string.collect_failed)
                } else {
                    c.toast(BaseConfig.TAG, R.string.cancel_collect_failed)
                }

                sendMessage(c, true, collectId, position, toCollect)

                getUiView()?.showErrorView()
                view?.failed(c, message, position, toCollect)
            }
        }

        result.position = index
        result.toCollect = !collect
        result.collectId = id

        if (collect) {
            //取消收藏
            unCollect(id, result)
        } else {
            collect(id, result)
        }

        return true
    }

    private fun sendMessage(context: Context, success: Boolean, collectId: Int, position: Int, toCollect: Boolean) {
        if (!sendMessage) {
            return
        }

        val intent = Intent()
        intent.action = BaseConfig.COLLECT_RESULT_ACTION
        intent.putExtra(BaseConfig.COLLECT_RESULT, success)
        intent.putExtra(BaseConfig.COLLECT_DATA_POSITION, position)
        intent.putExtra(BaseConfig.TO_COLLECT, toCollect)
        intent.putExtra(BaseConfig.COLLECT_ID, collectId)
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }

    private fun unCollect(id: Int, result: RequestResult<String>) {
        val ctx = getContext() ?: return
        httpHelper.unCollect(ctx, id, result)
    }

    private fun collect(id: Int, result: RequestResult<String>) {
        val ctx = getContext() ?: return
        httpHelper.collect(ctx, id, result)
    }
}

open class CollectView : BasicView<String>() {

    /**
     * 数据上报成功
     * @param position 收藏时的位置
     * @param toCollect 这次的操作是去收藏还是去取消收藏
     */
    open fun success(context: Context, position: Int, toCollect: Boolean) {
        super.success(true)
    }

    /**
     * 数据上报出错
     * @param string 错误信息
     * @param position  收藏时的位置
     * @param toCollect 这次的操作是去收藏还是去取消收藏
     */
    open fun failed(context: Context, string: String, position: Int, toCollect: Boolean) {
        super.failed(string)
    }
}