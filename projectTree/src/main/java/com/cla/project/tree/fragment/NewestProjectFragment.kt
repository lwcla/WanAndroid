package com.cla.project.tree.fragment

import com.cla.project.tree.presenter.ProjectPresenter
import com.cla.project.tree.presenter.ProjectView
import com.konsung.basic.ui.UiView

/**
 * 最新项目fragment
 */
class NewestProjectFragment : ProjectFragment() {

    override fun initProjectPresenter(view: ProjectView): ProjectPresenter = NewestPresenter(this, view)
}

class NewestPresenter(uiView: UiView?, view: ProjectView?) : ProjectPresenter(uiView, view) {

    override fun refresh() {
        refreshData = true
        page = 0
        request { ctx, result ->
            httpHelper.fetchNewestProject(ctx, page++, result)
        }
    }

    override fun loadMore() {
        refreshData = false
        request { ctx, result ->
            httpHelper.fetchNewestProject(ctx, page++, result)
        }
    }
}