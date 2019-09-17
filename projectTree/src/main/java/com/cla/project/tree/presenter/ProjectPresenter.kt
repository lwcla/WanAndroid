package com.cla.project.tree.presenter

import com.konsung.basic.bean.project.ProjectBean
import com.konsung.basic.ui.BasePresenter3
import com.konsung.basic.ui.BasicView
import com.konsung.basic.ui.UiView

abstract class ProjectPresenter(uiView: UiView?, view: ProjectView?) : BasePresenter3<ProjectBean, ProjectView>(uiView, view) {

}

open class ProjectView : BasicView<ProjectBean>()