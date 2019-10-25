package com.cla.project.tree.parent

import android.content.Context
import com.konsung.basic.bean.project.ProjectTitleBean
import com.konsung.basic.presenter.BasicPresenter2
import com.konsung.basic.presenter.BasicView
import com.konsung.basic.presenter.UiView

class LoadProjectTitle(uiView: UiView?, view: LoadProjectTitleView?) : BasicPresenter2<List<ProjectTitleBean>, LoadProjectTitleView>(uiView, view) {

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