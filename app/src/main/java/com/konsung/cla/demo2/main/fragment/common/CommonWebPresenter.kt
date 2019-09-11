package com.konsung.cla.demo2.main.fragment.common

import android.content.Context
import com.konsung.basic.bean.CommonWebBean
import com.konsung.basic.ui.BasePresenter2
import com.konsung.basic.ui.BasicView

class CommonWebPresenter(context: Context?, view: CommonWebView?) : BasePresenter2<List<CommonWebBean>, CommonWebView>(context, view) {

    fun load() {
        request { ctx, result ->
            httpHelper.loadCommonWeb(ctx, result)
        }
    }
}

open class CommonWebView : BasicView<List<CommonWebBean>>()