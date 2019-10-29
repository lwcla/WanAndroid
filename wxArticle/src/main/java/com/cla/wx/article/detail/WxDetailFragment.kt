package com.cla.wx.article.detail

import com.chad.library.adapter.base.BaseViewHolder
import com.cla.wx.article.R
import com.cla.wx.article.adapter.WxDetailAdapter
import com.konsung.basic.adapter.BasicDataQuickAdapter
import com.konsung.basic.presenter.BasicPresenter
import com.konsung.basic.presenter.Presenter
import com.konsung.basic.ui.fragment.HomeDataFragment

class WxDetailFragment : HomeDataFragment() {

    var cId = -1

    private val loadWxDetail by lazy { LoadWxDetail(this, homeView, cId) }

    override fun initPresenters(): List<BasicPresenter>? = listOf(collectPresenter, loadWxDetail)

    override fun initPresenterList(): List<Presenter>?  =  null

    override fun loadMoreData() {
        loadWxDetail.loadMore()
    }

    override fun initDataAdapter(): BasicDataQuickAdapter<BaseViewHolder>? = WxDetailAdapter(context!!, listOf())

    override fun resetData() {
        loadWxDetail.refresh()
    }

    override fun getImvStartId(): Int = R.id.imvStart
}