package com.cla.system.tree.detail

import com.konsung.basic.presenter.HomePresenter
import com.konsung.basic.presenter.HomeView
import com.konsung.basic.presenter.UiView


class LoadSystemTreeDetail(uiView: UiView?, view: HomeView?, private val cid: Int) : HomePresenter(uiView, view) {

    override fun refresh() {
        refresh { ctx, result ->
            httpHelper.fetchSystemTreeDetail(ctx, page, cid, result)
        }
    }

    override fun loadMore() {
        loadMore { ctx, result -> httpHelper.fetchSystemTreeDetail(ctx, page, cid, result) }
    }
}

