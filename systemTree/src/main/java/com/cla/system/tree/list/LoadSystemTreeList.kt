package com.cla.system.tree.list

import com.konsung.basic.bean.tree.SystemTreeListBean
import com.konsung.basic.ui.BasePresenter2
import com.konsung.basic.ui.BasicView
import com.konsung.basic.ui.UiView

class LoadSystemTreeList(uiView: UiView?, view: LoadSystemTreeListView?) : BasePresenter2<List<SystemTreeListBean>, LoadSystemTreeListView>(uiView, view) {

    fun refresh() {
        request { ctx, result -> httpHelper.fetchTreeList(ctx, result) }
    }
}

open class LoadSystemTreeListView : BasicView<List<SystemTreeListBean>>()