package com.konsung.cla.demo2.web

import android.content.Context
import com.konsung.basic.ui.BasePresenter
import com.konsung.basic.ui.BasicView

class CollectPresenter(view: CollectView?) : BasePresenter<String, CollectView>(view) {

    private var position = -1
    private var collect = false

    override fun success() {
        super.success()
        view?.success(position, !collect)
    }

    override fun failed(message: String) {
        super.failed(message)
        view?.failed(message, position, !collect)
    }

    /**
     * 上报收藏信息
     * @param context context
     * @param position 收藏的文章的位置
     * @param id 文章的id
     * @param collect 现在是收藏还是取消状态
     */
    fun collect(context: Context?, position: Int, id: Int, collect: Boolean) {
        this.position = position
        this.collect = collect

        if (collect) {
            //取消收藏
            unCollect(context, id)
        } else {
            collect(context, id)
        }
    }

    private fun unCollect(context: Context?, id: Int) {
        request(context) { ctx, result ->
            httpHelper.unCollect(ctx, id, result)
        }
    }

    private fun collect(context: Context?, id: Int) {
        request(context) { ctx, result ->
            httpHelper.collect(ctx, id, result)
        }
    }
}


open class CollectView : BasicView<String>() {

    /**
     * 数据上报成功
     * @param position 收藏时的位置
     * @param toCollect 这次的操作是去收藏还是去取消收藏
     */
    open fun success(position: Int, toCollect: Boolean) {
        super.success()
    }

    /**
     * 数据上报出错
     * @param string 错误信息
     * @param position  收藏时的位置
     * @param toCollect 这次的操作是去收藏还是去取消收藏
     */
    open fun failed(string: String, position: Int, toCollect: Boolean) {
        super.failed(string)
    }
}