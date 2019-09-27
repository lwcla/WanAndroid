package com.cla.wx.article.detail

import com.chad.library.adapter.base.BaseViewHolder
import com.cla.wx.article.R
import com.cla.wx.article.adapter.WxDetailAdapter
import com.konsung.basic.adapter.BasicDataQuickAdapter
import com.konsung.basic.bean.HomeData
import com.konsung.basic.ui.BasicPresenter
import com.konsung.basic.ui.HomeDataFragment
import com.konsung.basic.ui.HomeView
import com.konsung.basic.ui.RefreshRecyclerView

class WxDetailFragment : HomeDataFragment() {

    var cId = -1

    private val loadWxDetail by lazy { initLoadWxDetail() }

    override fun initPresenters(): List<BasicPresenter>? = listOf(collectPresenter, loadWxDetail)

    override fun loadMoreData() {
        loadWxDetail.loadMore()
    }

    override fun initDataAdapter(): BasicDataQuickAdapter<BaseViewHolder>? = WxDetailAdapter(context!!, listOf())

    private fun initLoadWxDetail(): LoadWxDetail {
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

        return LoadWxDetail(this, view, cId)
    }

    override fun resetData() {
        loadWxDetail.refresh()
    }

    override fun getImvStartId(): Int = R.id.imvStart
}