package com.cla.project.tree.fragment

import com.chad.library.adapter.base.BaseViewHolder
import com.cla.project.tree.ProjectAdapter
import com.cla.project.tree.R
import com.konsung.basic.adapter.BasicDataQuickAdapter
import com.konsung.basic.bean.HomeData
import com.konsung.basic.ui.BasicPresenter
import com.konsung.basic.ui.HomeDataFragment
import com.konsung.basic.ui.HomeView
import com.konsung.basic.ui.RefreshRecyclerView

abstract class ProjectFragment : HomeDataFragment() {

    private val projectView = object : HomeView() {

        override fun success(t: HomeData, refreshData: Boolean) {
            if (refreshData) {
                dataAdapter?.setNewData(t.beanList)
            } else {
                dataAdapter?.addData(t.beanList)
            }
        }

        override fun getRefreshRv(): RefreshRecyclerView? = refreshRv
    }

    private val presenter: ProjectPresenter by lazy { initProjectPresenter(projectView) }

    override fun initPresenters(): List<BasicPresenter>? = listOf(presenter, collectPresenter)

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