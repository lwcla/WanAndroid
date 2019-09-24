package com.konsung.cla.demo2.presenter

import com.konsung.basic.bean.project.ProjectBean
import com.konsung.basic.ui.BasePresenter3
import com.konsung.basic.ui.BasicView
import com.konsung.basic.ui.UiView
import com.konsung.basic.util.toast
import com.konsung.cla.demo2.R

class SearchResultPresenter(uiView: UiView?, view: SearchResultView?, private val key: String?) : BasePresenter3<ProjectBean, SearchResultView>(uiView, view) {

    companion object {
        val TAG: String = SearchResultPresenter::class.java.simpleName
    }

    override fun refresh() {

        if (key.isNullOrEmpty()) {
            getContext()?.toast(TAG, R.string.key_is_null)
            return
        }

        refresh { ctx, result -> httpHelper.fetchSearchResult(ctx, page, key, result) }
    }

    override fun loadMore() {

        if (key.isNullOrEmpty()) {
            getContext()?.toast(TAG, R.string.key_is_null)
            return
        }

        loadMore { ctx, result -> httpHelper.fetchSearchResult(ctx, page, key, result) }
    }

}

open class SearchResultView : BasicView<ProjectBean>()