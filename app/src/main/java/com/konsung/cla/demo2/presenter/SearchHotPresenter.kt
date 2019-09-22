package com.konsung.cla.demo2.presenter

import android.content.Context
import com.konsung.basic.bean.search.SearchHotKey
import com.konsung.basic.ui.BasePresenter2
import com.konsung.basic.ui.BasicView
import com.konsung.basic.ui.UiView

class SearchHotPresenter(uiView: UiView?, view: SearchHotView?) : BasePresenter2<List<SearchHotKey>, SearchHotView>(uiView, view) {

    override fun success(context: Context, t: List<SearchHotKey>) {

        val list = mutableListOf<SearchHotKey>()

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


open class SearchHotView : BasicView<List<SearchHotKey>>()