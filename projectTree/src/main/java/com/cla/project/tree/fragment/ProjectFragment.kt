package com.cla.project.tree.fragment

import com.chad.library.adapter.base.BaseViewHolder
import com.cla.project.tree.ProjectAdapter
import com.cla.project.tree.R
import com.konsung.basic.adapter.BasicDataQuickAdapter
import com.konsung.basic.presenter.BasicPresenter
import com.konsung.basic.ui.fragment.HomeDataFragment
import com.konsung.basic.presenter.HomeView
import com.konsung.basic.presenter.Presenter

abstract class ProjectFragment : HomeDataFragment() {

    private val presenter: ProjectPresenter by lazy { initProjectPresenter(homeView) }

    override fun initPresenters(): List<BasicPresenter>? = listOf(presenter, collectPresenter)

    override fun initPresenterList(): List<Presenter>?  =  null

    override fun initDataAdapter(): BasicDataQuickAdapter<BaseViewHolder>? = ProjectAdapter(context!!, listOf())

    override fun getImvEnvelopePicId(): Int = R.id.imvEnvelope

    override fun getImvStartId(): Int = R.id.imvStart

    override fun resetData() {
        presenter.refresh()
    }

    override fun loadMoreData() {
        presenter.loadMore()
    }

    abstract fun initProjectPresenter(view: HomeView): ProjectPresenter
}