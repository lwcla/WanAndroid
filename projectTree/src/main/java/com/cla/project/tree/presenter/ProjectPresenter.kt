package com.cla.project.tree.presenter

import android.content.Context
import com.cla.project.tree.R
import com.konsung.basic.bean.project.ProjectBean
import com.konsung.basic.ui.BasePresenter3
import com.konsung.basic.ui.BasicView
import com.konsung.basic.ui.UiView

abstract class ProjectPresenter(uiView: UiView?, view: ProjectView?) : BasePresenter3<ProjectBean, ProjectView>(uiView, view) {

    override fun success(context: Context, t: ProjectBean) {

        if (t.datas == null || t.datas!!.isEmpty()) {
            failed(context, context.getString(R.string.data_list_is_null))
            return
        }


        super.success(context, t)
    }
}

open class ProjectView : BasicView<ProjectBean>()