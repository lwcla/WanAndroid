package com.cla.system.tree.list

import com.konsung.basic.bean.tree.SystemTreeListBean
import com.konsung.basic.presenter.BasicPresenter2
import com.konsung.basic.presenter.BasicView
import com.konsung.basic.presenter.UiView

class LoadSystemTreeList(uiView: UiView?, view: LoadSystemTreeListView?) : BasicPresenter2<List<SystemTreeListBean>, LoadSystemTreeListView>(uiView, view) {

    fun refresh() {
        request { ctx, result -> httpHelper.fetchTreeList(ctx, result) }
    }
}

open class LoadSystemTreeListView : BasicView<List<SystemTreeListBean>>()