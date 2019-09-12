package com.cla.project.tree.fragment

import com.cla.project.tree.presenter.ProjectPresenter
import com.cla.project.tree.presenter.ProjectView
import com.konsung.basic.ui.UiView

/**
 * 项目分类
 */
class ProjectTreeFragment : ProjectFragment() {
    var cId = 0

    override fun initProjectPresenter(view: ProjectView): ProjectPresenter = ProjectTreePresenter(this, view, cId)
}

class ProjectTreePresenter(uiView: UiView?, view: ProjectView?, private val cId: Int = 0) : ProjectPresenter(uiView, view) {

    init {
        pageStart = 1
    }

    override fun refresh() {
        refreshData = true
        page = pageStart

        request { ctx, result -> httpHelper.fetchProjectTree(ctx, page, cId, result) }
    }

    override fun loadMore() {
        request { ctx, result -> httpHelper.fetchProjectTree(ctx, page, cId, result) }
    }
}