package com.cla.system.tree.detail

import com.chad.library.adapter.base.BaseViewHolder
import com.cla.system.tree.R
import com.cla.system.tree.detail.adapter.SystemTreeDetailAdapter
import com.konsung.basic.adapter.BasicDataQuickAdapter
import com.konsung.basic.bean.HomeData
import com.konsung.basic.ui.BasicPresenter
import com.konsung.basic.ui.HomeDataFragment
import com.konsung.basic.ui.HomeView
import com.konsung.basic.ui.RefreshRecyclerView

class SystemTreeDetailFragment : HomeDataFragment() {

    var cid = -1

    private val loadSystemTreeDetail by lazy { initLoadSystemTreeDetail() }

    override fun initPresenters(): List<BasicPresenter>? = listOf(collectPresenter, loadSystemTreeDetail)

    override fun initDataAdapter(): BasicDataQuickAdapter<BaseViewHolder>? = context?.let { SystemTreeDetailAdapter(it, mutableListOf()) }

    override fun loadMoreData() {
        loadSystemTreeDetail.loadMore()
    }

    private fun initLoadSystemTreeDetail(): LoadSystemTreeDetail {

        val view = object : HomeView() {

            override fun success(t: HomeData, refreshData: Boolean) {
                if (refreshData) {
                    dataAdapter?.setNewData(t.beanList)
                } else {
                    dataAdapter?.addData(t.beanList)
                }
            }

            override fun getRefreshRv(): RefreshRecyclerView? = refreshRv
        }

        return LoadSystemTreeDetail(this, view, cid)
    }

    override fun resetData() {
        loadSystemTreeDetail.refresh()
    }

    override fun getImvStartId(): Int = R.id.imvStart
}