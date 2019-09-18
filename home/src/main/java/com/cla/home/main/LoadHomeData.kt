package com.cla.home.main

import android.content.Context
import com.konsung.basic.bean.HomeData
import com.konsung.basic.ui.BasePresenter3
import com.konsung.basic.ui.BasicView
import com.konsung.basic.ui.UiView
import com.konsung.basic.util.Debug

class HomeDataPresenter(uiView: UiView?, view: LoadHomeView?) : BasePresenter3<HomeData, LoadHomeView>(uiView, view) {

    companion object {
        val TAG: String = HomeDataPresenter::class.java.simpleName
    }

    var over: Boolean = false
    private var homeData: HomeData? = null
    private var dataBeanList = mutableListOf<HomeData.DatasBean>()

    /**
     * 加载置顶数据
     */
    private val topArticlePresenter = object : TopArticlePresenter(uiView, TopArticleView()) {

        override fun success(context: Context, t: List<HomeData.DatasBean>) {
            dataBeanList.addAll(t)
            result(context)
        }

        override fun failed(context: Context, message: String) {
            dataBeanList.add(HomeData.DatasBean())
            result(context)
            super.failed(context, message)
        }

        override fun noNetwork(context: Context) {
            failed(context, "")
        }
    }

    /**
     * 加载首页数据以及置顶文章
     */
    fun loadWithTopData() {
        refresh()
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

            getUiView()?.showContentView()
            view?.success(context, homeData!!, refreshData)
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

        //需要首页和置顶文章数据一起加载完毕之后再回调success方法
        if (refreshData) {
            homeData = t
            result(context)
            return
        }

        getUiView()?.showContentView()
        view?.success(context, t, refreshData)
    }

    override fun refresh() {
        refreshData = true
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
     * 加载更多数据
     */
    override fun loadMore() {
        refreshData = false
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
     * @param refreshData 是否刷新所有数据
     */
    open fun success(context: Context, t: HomeData, refreshData: Boolean) {
        super.success(t, refreshData)
    }
}
