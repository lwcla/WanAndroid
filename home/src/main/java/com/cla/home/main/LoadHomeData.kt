package com.cla.home.main

import android.content.Context
import com.konsung.basic.bean.HomeData
import com.konsung.basic.presenter.HomePresenter
import com.konsung.basic.presenter.HomeView
import com.konsung.basic.presenter.UiView
import com.konsung.basic.presenter.UiViewAdapter
import com.konsung.basic.util.Debug

class HomeDataPresenter(uiView: UiView?, view: HomeView?) : HomePresenter(uiView, view) {

    companion object {
        val TAG: String = HomeDataPresenter::class.java.simpleName
    }

    private var homeData: HomeData? = null
    private var dataBeanList = mutableListOf<HomeData.DatasBean>()

    private var topUiView = object : UiViewAdapter() {
        override fun getUiContext(): Context? = uiView?.getUiContext()
    }

    /**
     * 加载置顶数据
     */
    private val topArticlePresenter = object : TopArticlePresenter(topUiView, TopArticleView()) {

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
        refresh()
    }

    /**
     * 返回结果
     */
    fun result(context: Context) {

        if (dataBeanList.size == 0) {
            return
        }

        homeData?.let {
            super.success(context, it)
        }
    }

    /**
     * 首页数据加载成功
     */
    override fun success(context: Context, t: HomeData) {

        //需要首页和置顶文章数据一起加载完毕之后再回调success方法
        if (refreshData) {
            homeData = t
            result(context)
            return
        }

        super.success(context, t)
    }

    override fun refresh() {
        homeData = null
        dataBeanList.clear()
        refresh { ctx, result -> httpHelper.loadHomeData(ctx, page, result) }
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
        Debug.info(TAG, "HomeDataPresenter loadMore over?$over page=$page")
        loadMore { ctx, result -> httpHelper.loadHomeData(ctx, page, result) }
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
