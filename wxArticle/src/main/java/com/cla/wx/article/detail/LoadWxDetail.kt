package com.cla.wx.article.detail

import com.konsung.basic.ui.HomePresenter
import com.konsung.basic.ui.HomeView
import com.konsung.basic.ui.UiView

class LoadWxDetail(uiView: UiView?, view: HomeView?, private val cId: Int) : HomePresenter(uiView, view) {

    override fun refresh() {
        refresh { ctx, result -> httpHelper.fetchWxArticleDetail(ctx, cId, page, result) }
    }

    override fun loadMore() {
        loadMore { ctx, result -> httpHelper.fetchWxArticleDetail(ctx, cId, page, result) }
    }
}
