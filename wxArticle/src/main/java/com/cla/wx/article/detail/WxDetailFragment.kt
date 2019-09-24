package com.cla.wx.article.detail

import android.widget.TextView
import com.cla.wx.article.R
import com.cla.wx.article.adapter.WxDetailAdapter
import com.konsung.basic.bean.HomeData
import com.konsung.basic.bean.project.ProjectBean
import com.konsung.basic.ui.BasicFragment
import com.konsung.basic.ui.BasicPresenter
import com.konsung.basic.util.AppUtils
import com.konsung.basic.util.toast

class WxDetailFragment : BasicFragment() {

    var cId = -1
    private var wxAdapter: WxDetailAdapter? = null
    private val loadWxDetail by lazy { initLoadWxDetail() }

    override fun getLayoutId(): Int = R.layout.view_fresh_rv

    override fun initPresenters(): List<BasicPresenter>? = listOf(collectPresenter, loadWxDetail)

    override fun initView() {
        refreshRv = showView?.findViewById(R.id.refreshRv)

        wxAdapter = WxDetailAdapter(context!!, listOf())
        dataListAdapterHelper = wxAdapter

        refreshRv?.initRecyclerView(wxAdapter, fragmentRefresh, index) {
            refreshRv?.autoRefresh()
            resetData()
        }
    }

    override fun initEvent() {
        refreshRv?.apply {

            setOnRefreshListener {
                resetData()
            }

            setOnLoadMoreListener {
                loadWxDetail.loadMore()
            }
        }

        wxAdapter?.apply {

            setOnItemClickListener { _, view, position ->
                val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
                val d = findDataByPosition(position)
                d?.apply {
                    AppUtils.startWebAty(activity, context, tvTitle, title, link, artId = id, dataPosition = position, collect = d.collect)
                }
            }

            setOnItemChildClickListener { _, view, position ->

                if (context == null) {
                    return@setOnItemChildClickListener
                }

                when (view.id) {
                    //收藏
                    R.id.imvStart -> {

                        val data = wxAdapter?.findDataByPosition(position)
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
                            wxAdapter?.refreshCollectStatus(position, data)
                        }
                    }
                }
            }
        }
    }

    override fun initData() {
        resetData()
    }

    private fun initLoadWxDetail(): LoadWxDetail {
        val view = object : LoadWxDetailView() {

            override fun success(t: ProjectBean, refreshData: Boolean) {

                loadWxDetail.page = t.curPage

                refreshRv?.finishRefresh(200)
                refreshRv?.finishLoadMore(200, true, t.over)

                val list = mutableListOf<HomeData.DatasBean>()
                list.addAll(t.datas!!.filterNotNull())

                if (refreshData) {
                    wxAdapter?.setNewData(list)
                } else {
                    wxAdapter?.addData(list)
                }
            }
        }

        return LoadWxDetail(this, view, cId)
    }

    override fun resetData() {
        loadWxDetail.refresh()
    }

    override fun refreshView() {
        refreshRv?.refreshDataAfterScrollTop()
    }

}