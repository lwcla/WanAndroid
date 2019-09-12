package com.konsung.cla.demo2.main.fragment.common

import com.konsung.basic.bean.CommonWebBean
import com.konsung.basic.ui.BasePresenter2
import com.konsung.basic.ui.BasicView
import com.konsung.basic.ui.UiView

class CommonWebPresenter(uiView: UiView?, view: CommonWebView?) : BasePresenter2<List<CommonWebBean>, CommonWebView>(uiView, view) {

    fun load() {
        request { ctx, result ->
            httpHelper.loadCommonWeb(ctx, result)
        }
    }
}

open class CommonWebView : BasicView<List<CommonWebBean>>()