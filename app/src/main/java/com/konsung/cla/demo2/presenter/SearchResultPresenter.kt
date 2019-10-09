package com.konsung.cla.demo2.presenter

import com.konsung.basic.ui.HomePresenter
import com.konsung.basic.ui.HomeView
import com.konsung.basic.ui.UiView
import com.konsung.basic.util.toast
import com.konsung.cla.demo2.R

class SearchResultPresenter(uiView: UiView?, view: HomeView?, private val key: String?, private val wxId: Int) : HomePresenter(uiView, view) {

    companion object {
        val TAG: String = SearchResultPresenter::class.java.simpleName
    }

    override fun refresh() {

        if (key.isNullOrEmpty()) {
            getContext()?.toast(TAG, R.string.key_is_null)
            return
        }

        if (wxId < 0) {
            refresh { ctx, result -> httpHelper.fetchSearchResult(ctx, page, key, result) }
        } else {
            refresh { ctx, result -> httpHelper.fetchWxSearchResult(ctx, wxId, page, key, result) }
        }
    }

    override fun loadMore() {

        if (key.isNullOrEmpty()) {
            getContext()?.toast(TAG, R.string.key_is_null)
            return
        }

        if (wxId < 0) {
            loadMore { ctx, result -> httpHelper.fetchSearchResult(ctx, page, key, result) }
        } else {
            loadMore { ctx, result -> httpHelper.fetchWxSearchResult(ctx, wxId, page, key, result) }
        }
    }

}

