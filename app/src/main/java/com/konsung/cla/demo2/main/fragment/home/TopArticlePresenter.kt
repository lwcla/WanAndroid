package com.konsung.cla.demo2.main.fragment.home

import android.content.Context
import com.konsung.basic.bean.HomeData
import com.konsung.basic.ui.BasePresenter
import com.konsung.basic.ui.BasicView


open class TopArticlePresenter(context: Context?, view: TopArticleView) : BasePresenter<List<HomeData.DatasBean>, TopArticleView>(context, view) {

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