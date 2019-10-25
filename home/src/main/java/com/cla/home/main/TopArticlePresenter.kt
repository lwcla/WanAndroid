package com.cla.home.main

import com.konsung.basic.bean.HomeData
import com.konsung.basic.presenter.BasicPresenter2
import com.konsung.basic.presenter.BasicView
import com.konsung.basic.presenter.UiView


open class TopArticlePresenter(uiView: UiView?, view: TopArticleView) : BasicPresenter2<List<HomeData.DatasBean>, TopArticleView>(uiView, view) {

    companion object {
        val TAG: String = TopArticlePresenter::class.java.simpleName
    }

    fun load() {
        request { ctx, result ->
            httpHelper.loadTopArticle(ctx, result)
        }
    }
}

open class TopArticleView : BasicView<List<HomeData.DatasBean>>()