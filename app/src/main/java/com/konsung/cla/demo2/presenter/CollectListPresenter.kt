package com.konsung.cla.demo2.presenter

import android.content.Context
import com.konsung.basic.bean.HomeData
import com.konsung.basic.presenter.HomePresenter
import com.konsung.basic.presenter.HomeView
import com.konsung.basic.presenter.UiView

class CollectListPresenter(uiView: UiView?, view: HomeView) : HomePresenter(uiView, view) {

    override fun success(context: Context, t: HomeData) {

        /**
         * 把所有的数据全部设置为已经收藏
         */
        t.datas?.let {
            for (bean in it) {
                bean?.apply {
                    collect = true
                }
            }
        }

        super.success(context, t)
    }

    override fun refresh() {
        refresh { ctx, result -> httpHelper.fetchCollectList(ctx, page, result) }
    }

    override fun loadMore() {
        loadMore { ctx, result -> httpHelper.fetchCollectList(ctx, page, result) }
    }
}
