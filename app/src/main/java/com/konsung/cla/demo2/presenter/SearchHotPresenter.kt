package com.konsung.cla.demo2.presenter

import android.content.Context
import com.konsung.basic.bean.search.SearchKey
import com.konsung.basic.presenter.BasicPresenter2
import com.konsung.basic.presenter.BasicView
import com.konsung.basic.presenter.UiView

class SearchHotPresenter(uiView: UiView?, view: SearchHotView?) : BasicPresenter2<List<SearchKey>, SearchHotView>(uiView, view) {

    override fun success(context: Context, t: List<SearchKey>) {

        val list = mutableListOf<SearchKey>()

        for (key in t) {

            if (key.name.isNullOrEmpty()) {
                continue
            }

            list.add(key)
        }

        super.success(context, list)
    }

    fun load() {
        request { ctx, result -> httpHelper.fetchSearchHotKey(ctx, result) }
    }
}


open class SearchHotView : BasicView<List<SearchKey>>()