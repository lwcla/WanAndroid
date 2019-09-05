package com.konsung.cla.demo2.main.fragment.home

import android.content.Context
import com.konsung.basic.bean.HomeData
import com.konsung.basic.ui.BasePresenter
import com.konsung.basic.ui.BasicView
import com.konsung.basic.util.Debug

class HomeDataPresenter(view: LoadHomeView?) : BasePresenter<HomeData, LoadHomeView>(view) {

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
    private val topArticlePresenter = object : TopArticlePresenter(TopArticleView()) {
        override fun success(t: List<HomeData.DatasBean>) {
            dataBeanList.addAll(t)
            result()
        }
    }

    /**
     * 加载首页数据以及置顶文章
     */
    fun loadWithTopData(context: Context?) {

        withTop = true
        page = 0
        over = false

        homeData = null
        dataBeanList.clear()

        topArticlePresenter.load(context) //置顶文章
        loadHomeData(context) //首页数据
    }

    /**
     * 返回结果
     */
    fun result() {

        if (dataBeanList.size == 0) {
            return
        }

        homeData?.apply {
            val list = mutableListOf<HomeData.DatasBean>()

            datas?.let { list.addAll(it) }
            list.addAll(0, dataBeanList)

            datas = list
            view?.success(homeData!!)
        }
    }

    /**
     * 首页数据加载成功
     */
    override fun success(t: HomeData) {
        if (t.datas == null || t.datas!!.isEmpty()) {
            view?.failed("")
            return
        }

        if (withTop) {
            homeData = t
            result()
            return
        }

        super.success(t)
    }

    /**
     * 加载更多数据
     */
    fun loadMore(context: Context?) {
        withTop = false
        loadHomeData(context)
    }

    /**
     * 加载首页数据
     */
    private fun loadHomeData(context: Context?) {

        Debug.info(TAG, "HomeDataPresenter loadMore over?$over page=$page")

        if (over) {
            return
        }
        request(context) { ctx, result ->
            httpHelper.loadHomeData(ctx, page++, result)
        }
    }

    override fun destroy() {
        topArticlePresenter.destroy()
        super.destroy()
    }
}

open class LoadHomeView : BasicView<HomeData>()
