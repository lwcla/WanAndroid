package com.konsung.cla.demo2.main.fragment.home

import android.content.Context
import com.konsung.basic.bean.HomeData
import com.konsung.basic.ui.BasePresenter
import com.konsung.basic.ui.BasicView


open class TopArticlePresenter(view: TopArticleView) : BasePresenter<List<HomeData.DatasBean>, TopArticleView>(view) {

    fun load(context: Context?) {
        request(context) { ctx, result ->
            httpHelper.loadTopArticle(ctx, result)
        }
    }
}

open class TopArticleView : BasicView<List<HomeData.DatasBean>>()