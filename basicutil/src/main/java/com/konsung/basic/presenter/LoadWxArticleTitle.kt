package com.konsung.basic.presenter

import com.konsung.basic.bean.project.ProjectTitleBean
import com.konsung.basic.ui.BasePresenter2
import com.konsung.basic.ui.BasicView
import com.konsung.basic.ui.UiView

class LoadWxArticleTitle(uiView: UiView?, view: LoadWxArticleTitleView?) : BasePresenter2<List<ProjectTitleBean>, LoadWxArticleTitleView>(uiView, view) {

    fun load() {
        request { ctx, result -> httpHelper.fetchWxArticleTitle(ctx, result) }
    }
}

open class LoadWxArticleTitleView : BasicView<List<ProjectTitleBean>>()