package com.konsung.cla.demo2.presenter

import com.konsung.basic.bean.HomeData
import com.konsung.basic.ui.BasePresenter3
import com.konsung.basic.ui.BasicView
import com.konsung.basic.ui.UiView

class SearchResultPresenter(uiView: UiView?, view: SearchResultView?, private val key: String) : BasePresenter3<List<HomeData.DatasBean>, SearchResultView>(uiView, view) {

    override fun refresh() {
        refresh { ctx, result -> httpHelper.fetchSearchResult(ctx, page, key, result) }
    }

    override fun loadMore() {
        loadMore { ctx, result -> httpHelper.fetchSearchResult(ctx, page, key, result) }
    }

}

open class SearchResultView : BasicView<List<HomeData.DatasBean>>()