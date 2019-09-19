package com.cla.project.tree.parent

import android.content.Context
import com.cla.project.tree.R
import com.konsung.basic.bean.project.ProjectTitleBean
import com.konsung.basic.ui.BasePresenter2
import com.konsung.basic.ui.BasicView
import com.konsung.basic.ui.UiView

class LoadProjectTitle(uiView: UiView?, view: LoadProjectTitleView?) : BasePresenter2<List<ProjectTitleBean>, LoadProjectTitleView>(uiView, view) {

    override fun success(context: Context, t: List<ProjectTitleBean>) {
        val beanList = mutableListOf<ProjectTitleBean>()
        beanList.addAll(t.filter { it.id > 0 })
        super.success(context, beanList)
    }

    fun load() {
        request { ctx, result ->
            httpHelper.loadProjectTitle(ctx, result)
        }
    }
}


open class LoadProjectTitleView : BasicView<List<ProjectTitleBean>>()