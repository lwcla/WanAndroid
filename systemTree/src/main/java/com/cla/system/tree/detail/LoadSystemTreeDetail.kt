package com.cla.system.tree.detail

import android.content.Context
import com.cla.system.tree.R
import com.konsung.basic.bean.project.ProjectBean
import com.konsung.basic.ui.BasePresenter3
import com.konsung.basic.ui.BasicView
import com.konsung.basic.ui.UiView


class LoadSystemTreeDetail(uiView: UiView?, view: LoadSystemTreeDetailView?, private val cid: Int) : BasePresenter3<ProjectBean, LoadSystemTreeDetailView>(uiView, view) {

    override fun success(context: Context, t: ProjectBean) {
        if (t.datas == null || t.datas!!.isEmpty()) {
            failed(context, context.getString(R.string.data_list_is_null))
            return
        }

        super.success(context, t)
    }

    override fun refresh() {
        refresh { ctx, result ->
            httpHelper.fetchSystemTreeDetail(ctx, page, cid, result)
        }
    }

    override fun loadMore() {
        loadMore { ctx, result -> httpHelper.fetchSystemTreeDetail(ctx, page, cid, result) }
    }

}

open class LoadSystemTreeDetailView : BasicView<ProjectBean>()