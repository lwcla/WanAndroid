package com.konsung.cla.demo2.main.fragment.home

import android.content.Context
import com.konsung.basic.bean.HomeData
import com.konsung.basic.ui.BasePresenter
import com.konsung.basic.ui.BasicView
import com.konsung.basic.util.Debug

class HomeDataPresenter(context: Context?, view: LoadHomeView?) : BasePresenter<HomeData, LoadHomeView>(context, view) {

    companion object {
        val TAG: String = HomeDataPresenter::class.java.simpleName
    }

    var page: Int = 0
    var over: Boolean = false
    private var homeData: HomeData? = null
    private var dataBeanList = mutableListOf<HomeData.DatasBean>()
    private var withTop = false

    /**
     * 加载置顶数据
     */
    private val topArticlePresenter = object : TopArticlePresenter(context, TopArticleView()) {
        override fun success(context: Context, t: List<HomeData.DatasBean>) {
            dataBeanList.addAll(t)
            result(context)
        }

        override fun failed(context: Context, message: String) {
            dataBeanList.add(HomeData.DatasBean())
            result(context)
            super.failed(context, message)
        }
    }

    /**
     * 加载首页数据以及置顶文章
     */
    fun loadWithTopData() {

        withTop = true
        page = 0
        over = false

        homeData = null
        dataBeanList.clear()

        loadHomeData() //首页数据
        //置顶文章
        // 这一句要放在获取首页数据的下面，因为获取首页数据的时候会调用result?.stop方法去停止之前的请求
        //同时也会调用topArticlePresenter中的result?.stop方法，如果放在loadHomeData上面的话
        //就会因为topArticlePresenter中的result不为空，停止数据请求，不会返回数据请求的结果
        topArticlePresenter.load()
    }

    /**
     * 返回结果
     */
    fun result(context: Context) {

        if (dataBeanList.size == 0) {
            return
        }

        homeData?.apply {
            val list = mutableListOf<HomeData.DatasBean>()

            list.addAll(0, dataBeanList.filter { it.id > 0 })
            datas?.let { list.addAll(it) }

            datas = list
            view?.success(context, homeData!!, withTop)
        }
    }

    /**
     * 首页数据加载成功
     */
    override fun success(context: Context, t: HomeData) {
        if (t.datas == null || t.datas!!.isEmpty()) {
            view?.failed("")
            return
        }

        //需要首页和banner数据一起加载完毕之后再回调success方法
        if (withTop) {
            homeData = t
            result(context)
            return
        }

        view?.success(context, t, withTop)
    }

    /**
     * 加载更多数据
     */
    fun loadMore() {
        withTop = false
        loadHomeData()
    }

    /**
     * 加载首页数据
     */
    private fun loadHomeData() {

        Debug.info(TAG, "HomeDataPresenter loadMore over?$over page=$page")

        if (over) {
            return
        }
        request { ctx, result ->
            httpHelper.loadHomeData(ctx, page++, result)
        }
    }

    override fun destroy() {
        topArticlePresenter.destroy()
        super.destroy()
    }

    override fun stop() {
        topArticlePresenter.stop()
        super.stop()
    }
}

open class LoadHomeView : BasicView<HomeData>() {
    /**
     * 获取数据成功
     * @param t 数据
     * @param refreshAll 是否刷新所有数据
     */
    open fun success(context: Context, t: HomeData, refreshAll: Boolean) {
        super.success(t)
    }
}
