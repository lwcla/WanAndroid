package com.konsung.basic.presenter

import com.konsung.basic.bean.project.ProjectTitleBean

class LoadWxArticleTitle(uiView: UiView?, view: LoadWxArticleTitleView?) : BasicPresenter2<List<ProjectTitleBean>, LoadWxArticleTitleView>(uiView, view) {

    fun load() {
        request { ctx, result -> httpHelper.fetchWxArticleTitle(ctx, result) }
    }
}

open class LoadWxArticleTitleView : BasicView<List<ProjectTitleBean>>()