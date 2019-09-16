package com.cla.system.tree.list

import android.content.Context
import com.cla.system.tree.R
import com.konsung.basic.bean.tree.SystemTreeListBean
import com.konsung.basic.ui.BasePresenter2
import com.konsung.basic.ui.BasicView
import com.konsung.basic.ui.UiView

class LoadSystemTreeList(uiView: UiView?, view: LoadSystemTreeListView?) : BasePresenter2<List<SystemTreeListBean>, LoadSystemTreeListView>(uiView, view) {

    override fun success(context: Context, t: List<SystemTreeListBean>) {

        if (t.isEmpty()) {
            super.failed(context, context.getString(R.string.data_list_is_null))
            return
        }

        super.success(context, t)
    }

    fun refresh() {
        request { ctx, result -> httpHelper.fetchTreeList(ctx, result) }
    }
}

open class LoadSystemTreeListView : BasicView<List<SystemTreeListBean>>()