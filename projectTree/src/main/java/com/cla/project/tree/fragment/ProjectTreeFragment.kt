package com.cla.project.tree.fragment

import com.konsung.basic.presenter.HomeView
import com.konsung.basic.presenter.UiView

/**
 * 项目分类
 */
class ProjectTreeFragment : ProjectFragment() {
    var cId = 0
    override fun initProjectPresenter(view: HomeView): ProjectPresenter = ProjectTreePresenter(this, view, cId)
}

class ProjectTreePresenter(uiView: UiView?, view: HomeView?, private val cId: Int = 0) : ProjectPresenter(uiView, view) {

    init {
        pageStart = 1
    }

    override fun refresh() {
        refresh { ctx, result -> httpHelper.fetchProjectTree(ctx, page, cId, result) }
    }

    override fun loadMore() {
        loadMore { ctx, result -> httpHelper.fetchProjectTree(ctx, page, cId, result) }
    }
}