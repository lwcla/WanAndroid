package com.cla.system.tree.detail

import android.widget.TextView
import com.cla.system.tree.R
import com.cla.system.tree.detail.adapter.SystemTreeDetailAdapter
import com.konsung.basic.bean.HomeData
import com.konsung.basic.ui.BasicFragment
import com.konsung.basic.ui.BasicPresenter
import com.konsung.basic.util.AppUtils
import com.konsung.basic.util.toast

class SystemTreeDetailFragment : BasicFragment() {

    var cid = -1

    private val systemAdapter by lazy { context?.let { SystemTreeDetailAdapter(it, mutableListOf()) } }
    private val loadSystemTreeDetail by lazy { initLoadSystemTreeDetail() }

    override fun getLayoutId(): Int = R.layout.view_fresh_rv

    override fun initPresenters(): List<BasicPresenter>? = listOf(collectPresenter, loadSystemTreeDetail)

    override fun initView() {
        refreshRv = showView?.findViewById(R.id.refreshRv)
        dataListAdapterHelper = systemAdapter
        refreshRv?.initRecyclerView(systemAdapter, fragmentRefresh, index, false) {
            refreshRv?.autoRefresh()
            resetData()
        }
    }

    override fun initEvent() {
        systemAdapter?.apply {
            setOnItemClickListener { _, view, position ->
                val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
                val d = findDataByPosition(position)
                d?.apply {
                    AppUtils.startWebAty(activity, context, tvTitle, title, link, artId = id, dataPosition = position, collect = d.collect)
                }
            }

            setOnItemChildClickListener { _, view, position ->

                if (activity == null) {
                    return@setOnItemChildClickListener
                }

                when (view.id) {

                    //收藏
                    R.id.imvStart -> {

                        val data = systemAdapter?.findDataByPosition(position)
                        if (data == null) {
                            toast(TAG, R.string.data_error)
                            return@setOnItemChildClickListener
                        }

                        val id = data.id
                        if (id < 0) {
                            toast(TAG, R.string.data_error)
                            return@setOnItemChildClickListener
                        }

                        val b = collectPresenter.collect(position, id, data.collect)
                        if (b) {
                            systemAdapter?.refreshCollectStatus(position, data)
                        }
                    }

                }
            }
        }

        refreshRv?.apply {

            setOnRefreshListener {
                resetData()
            }

            setOnLoadMoreListener {
                loadSystemTreeDetail.loadMore()
            }
        }
    }

    override fun initData() {
        resetData()
    }

    private fun initLoadSystemTreeDetail(): LoadSystemTreeDetail {
        val view = object : LoadSystemTreeDetailView() {

            override fun success(t: HomeData, refreshData: Boolean) {

                if (t.datas?.size ?: 0 == 0) {
                    refreshRv?.finishLoadMore(0, true, true)
                    return
                }

                loadSystemTreeDetail.page = t.curPage
                fetSuccess(t.over)

                val list = mutableListOf<HomeData.DatasBean>()
                t.datas?.let {
                    list.addAll(it.filterNotNull())
                }

                if (refreshData) {
                    systemAdapter?.setNewData(list)
                } else {
                    systemAdapter?.addData(list)
                }
            }
        }

        return LoadSystemTreeDetail(this, view, cid)
    }

    override fun resetData() {
        loadSystemTreeDetail.refresh()
    }
}