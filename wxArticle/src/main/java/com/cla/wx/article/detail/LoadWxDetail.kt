package com.cla.wx.article.detail

import com.konsung.basic.bean.HomeData
import com.konsung.basic.ui.BasePresenter3
import com.konsung.basic.ui.BasicView
import com.konsung.basic.ui.UiView

class LoadWxDetail(uiView: UiView?, view: LoadWxDetailView?, private val cId: Int) : BasePresenter3<HomeData, LoadWxDetailView>(uiView, view) {

    override fun refresh() {
        refresh { ctx, result -> httpHelper.fetchWxArticleDetail(ctx, cId, page, result) }
    }

    override fun loadMore() {
        loadMore { ctx, result -> httpHelper.fetchWxArticleDetail(ctx, cId, page, result) }
    }

}

open class LoadWxDetailView : BasicView<HomeData>()