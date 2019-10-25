package com.cla.wx.article.detail

import com.konsung.basic.presenter.HomePresenter
import com.konsung.basic.presenter.HomeView
import com.konsung.basic.presenter.UiView

class LoadWxDetail(uiView: UiView?, view: HomeView?, private val cId: Int) : HomePresenter(uiView, view) {

    init {
        pageStart = 1
    }

    override fun refresh() {
        refresh { ctx, result -> httpHelper.fetchWxArticleDetail(ctx, cId, page, result) }
    }

    override fun loadMore() {
        loadMore { ctx, result -> httpHelper.fetchWxArticleDetail(ctx, cId, page, result) }
    }
}
