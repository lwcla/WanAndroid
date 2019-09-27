package com.cla.project.tree.fragment

import com.konsung.basic.ui.HomeView
import com.konsung.basic.ui.UiView

/**
 * 最新项目fragment
 */
class NewestProjectFragment : ProjectFragment() {

    override fun initProjectPresenter(view: HomeView): ProjectPresenter = NewestPresenter(this, view)
}

class NewestPresenter(uiView: UiView?, view: HomeView?) : ProjectPresenter(uiView, view) {

    override fun refresh() {
        refresh { ctx, result -> httpHelper.fetchNewestProject(ctx, page, result) }
    }

    override fun loadMore() {
        loadMore { ctx, result -> httpHelper.fetchNewestProject(ctx, page, result) }
    }
}