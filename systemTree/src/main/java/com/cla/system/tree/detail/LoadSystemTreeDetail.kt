package com.cla.system.tree.detail

import com.konsung.basic.bean.HomeData
import com.konsung.basic.ui.BasePresenter3
import com.konsung.basic.ui.BasicView
import com.konsung.basic.ui.UiView


class LoadSystemTreeDetail(uiView: UiView?, view: LoadSystemTreeDetailView?, private val cid: Int) : BasePresenter3<HomeData, LoadSystemTreeDetailView>(uiView, view) {

    override fun refresh() {
        refresh { ctx, result ->
            httpHelper.fetchSystemTreeDetail(ctx, page, cid, result)
        }
    }

    override fun loadMore() {
        loadMore { ctx, result -> httpHelper.fetchSystemTreeDetail(ctx, page, cid, result) }
    }

}

open class LoadSystemTreeDetailView : BasicView<HomeData>()