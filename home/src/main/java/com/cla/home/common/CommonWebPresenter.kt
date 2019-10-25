package com.cla.home.common

import com.konsung.basic.bean.CommonWebBean
import com.konsung.basic.presenter.BasicPresenter2
import com.konsung.basic.presenter.BasicView
import com.konsung.basic.presenter.UiView

class CommonWebPresenter(uiView: UiView?, view: CommonWebView?) : BasicPresenter2<List<CommonWebBean>, CommonWebView>(uiView, view) {

    fun load() {
        request { ctx, result ->
            httpHelper.loadCommonWeb(ctx, result)
        }
    }
}

open class CommonWebView : BasicView<List<CommonWebBean>>()