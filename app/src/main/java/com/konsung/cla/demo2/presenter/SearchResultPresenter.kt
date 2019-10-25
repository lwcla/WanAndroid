package com.konsung.cla.demo2.presenter

import com.konsung.basic.presenter.HomePresenter
import com.konsung.basic.presenter.HomeView
import com.konsung.basic.presenter.UiView
import com.konsung.basic.util.toast
import com.konsung.cla.demo2.R
import com.konsung.cla.demo2.dialog.ChooseWxSearchDialog

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
            return
        }

        if (wxId == ChooseWxSearchDialog.AUTHOR_NAME) {
            //选择用作者昵称筛选
            refresh { ctx, result -> httpHelper.fetchSearchResultByAuthor(ctx, page, key, result) }
            return
        }

        //公众号搜索
        refresh { ctx, result -> httpHelper.fetchWxSearchResult(ctx, wxId, page, key, result) }
    }

    override fun loadMore() {

        if (key.isNullOrEmpty()) {
            getContext()?.toast(TAG, R.string.key_is_null)
            return
        }

        if (wxId < 0) {
            loadMore { ctx, result -> httpHelper.fetchSearchResult(ctx, page, key, result) }
            return
        }

        if (wxId == ChooseWxSearchDialog.AUTHOR_NAME) {
            //选择用作者昵称筛选
            loadMore { ctx, result -> httpHelper.fetchSearchResultByAuthor(ctx, page, key, result) }
            return
        }

        //公众号搜索
        loadMore { ctx, result -> httpHelper.fetchWxSearchResult(ctx, wxId, page, key, result) }
    }
}

