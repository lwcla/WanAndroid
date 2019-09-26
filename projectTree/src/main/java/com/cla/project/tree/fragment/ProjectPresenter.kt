package com.cla.project.tree.fragment

import com.konsung.basic.bean.HomeData
import com.konsung.basic.ui.BasePresenter3
import com.konsung.basic.ui.BasicView
import com.konsung.basic.ui.UiView

abstract class ProjectPresenter(uiView: UiView?, view: ProjectView?) : BasePresenter3<HomeData, ProjectView>(uiView, view) {

}

open class ProjectView : BasicView<HomeData>()