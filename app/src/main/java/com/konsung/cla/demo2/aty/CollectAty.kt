package com.konsung.cla.demo2.aty

import android.view.View
import android.view.ViewStub
import android.widget.TextView
import com.chad.library.adapter.base.BaseViewHolder
import com.konsung.basic.adapter.BasicDataQuickAdapter
import com.konsung.basic.bean.HomeData
import com.konsung.basic.ui.BasicPresenter
import com.konsung.basic.ui.HomeDataAty
import com.konsung.basic.util.toast
import com.konsung.cla.demo2.R
import com.konsung.cla.demo2.adapter.SearchResultAdapter
import com.konsung.cla.demo2.presenter.CollectListPresenter

/**
 * 我的收藏
 */
class CollectAty : HomeDataAty(), View.OnClickListener {

    companion object {
        val TAG: String = CollectAty::class.java.simpleName
    }

    init {
        refreshAfterScrollTop = false
        isCollectListPage = true
    }

    private val viewStub by lazy { findViewById<ViewStub>(R.id.viewStub) }

    private val collectListPresenter by lazy { CollectListPresenter(this, homeView) }

    override fun initPresenter(): List<BasicPresenter>? = listOf(collectListPresenter, collectPresenter)

    override fun initView() {
        viewStub.layoutResource = R.layout.view_collect_title
        val view = viewStub.inflate()
        val addCollect = view.findViewById<TextView>(R.id.addCollect)
        addCollect.setOnClickListener(this)
        super.initView()
    }

    override fun getAtyTitle(): String = getString(R.string.my_collect)

    override fun getImvStartId(): Int = R.id.imvStart

    override fun initAdapter(t: List<HomeData.DatasBean>): BasicDataQuickAdapter<BaseViewHolder> = SearchResultAdapter(context, t)

    override fun loadMoreData() {
        collectListPresenter.loadMore()
    }

    override fun resetData() {
        collectListPresenter.refresh()
    }

    override fun onClick(v: View) {

        when (v.id) {
            //添加收藏
            R.id.addCollect -> {
                toast(TAG, "点击收藏")
            }
        }
    }
}