package com.cla.system.tree.detail

import com.chad.library.adapter.base.BaseViewHolder
import com.cla.system.tree.R
import com.cla.system.tree.detail.adapter.SystemTreeDetailAdapter
import com.konsung.basic.adapter.BasicDataQuickAdapter
import com.konsung.basic.ui.BasicPresenter
import com.konsung.basic.ui.HomeDataFragment

class SystemTreeDetailFragment : HomeDataFragment() {

    var cid = -1

    private val loadSystemTreeDetail by lazy { LoadSystemTreeDetail(this, homeView, cid) }

    override fun initPresenters(): List<BasicPresenter>? = listOf(collectPresenter, loadSystemTreeDetail)

    override fun initDataAdapter(): BasicDataQuickAdapter<BaseViewHolder>? = context?.let { SystemTreeDetailAdapter(it, mutableListOf()) }

    override fun loadMoreData() {
        loadSystemTreeDetail.loadMore()
    }

    override fun resetData() {
        loadSystemTreeDetail.refresh()
    }

    override fun getImvStartId(): Int = R.id.imvStart
}