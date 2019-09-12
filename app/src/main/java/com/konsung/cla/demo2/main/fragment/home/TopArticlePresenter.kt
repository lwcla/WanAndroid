package com.konsung.cla.demo2.main.fragment.home

import com.konsung.basic.bean.HomeData
import com.konsung.basic.ui.BasePresenter2
import com.konsung.basic.ui.BasicView
import com.konsung.basic.ui.UiView


open class TopArticlePresenter(uiView: UiView?, view: TopArticleView) : BasePresenter2<List<HomeData.DatasBean>, TopArticleView>(uiView, view) {

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