package com.cla.system.tree.detail

import android.content.Context
import android.widget.TextView
import com.cla.system.tree.R
import com.cla.system.tree.detail.adapter.SystemTreeDetailAdapter
import com.konsung.basic.bean.HomeData
import com.konsung.basic.bean.project.ProjectBean
import com.konsung.basic.presenter.CollectPresenter
import com.konsung.basic.presenter.CollectView
import com.konsung.basic.ui.BasicFragment
import com.konsung.basic.ui.BasicPresenter
import com.konsung.basic.util.AppUtils
import com.konsung.basic.util.Debug
import com.konsung.basic.util.toast

class SystemTreeDetailFragment : BasicFragment() {

    var cid = -1

    private val systemAdapter by lazy { context?.let { SystemTreeDetailAdapter(it, mutableListOf()) } }
    private val collectPresenter by lazy { initCollectPresenter() }
    private val loadSystemTreeDetail by lazy { initLoadSystemTreeDetail() }

    override fun getLayoutId(): Int = R.layout.fragment_system_tree_detail

    override fun initPresenters(): List<BasicPresenter>? = listOf(collectPresenter, loadSystemTreeDetail)

    override fun initView() {
        refreshRv = showView?.findViewById(R.id.refreshRv)
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

                if (context == null) {
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

    private fun initCollectPresenter(): CollectPresenter {

        val view = object : CollectView() {

            override fun failed(context: Context, string: String, position: Int, toCollect: Boolean) {
                systemAdapter?.refreshCollectStatus(position, !toCollect)
            }
        }

        return CollectPresenter(this, view)
    }

    private fun initLoadSystemTreeDetail(): LoadSystemTreeDetail {
        val view = object : LoadSystemTreeDetailView() {

            override fun success(t: ProjectBean, refreshData: Boolean) {

                if (t.datas?.size ?: 0 == 0) {
                    refreshRv?.finishLoadMore(0, true, true)
                    return
                }

                loadSystemTreeDetail.page = t.curPage

                refreshRv?.finishRefresh(200)
                refreshRv?.finishLoadMore(200, true, t.over)

                val list = mutableListOf<HomeData.DatasBean>()
                list.addAll(t.datas!!.filterNotNull())

                if (refreshData) {
                    systemAdapter?.setNewData(list)
                } else {
                    systemAdapter?.addData(list)
                }
            }
        }

        return LoadSystemTreeDetail(this, view, cid)
    }

    override fun collectResult(success: Boolean, collectId: Int, position: Int, toCollect: Boolean) {
        Debug.info(TAG, "HomeFragment collectResult success?$success collectId=$collectId position=$position toCollect?$toCollect")

        if (context == null) {
            return
        }

        if (!success) {
            return
        }

        val data = systemAdapter?.findDataByPosition(position) ?: return

        if (data.id != collectId) {
            return
        }

        systemAdapter?.refreshCollectStatus(position, toCollect)
    }

    override fun resetData() {
        loadSystemTreeDetail.refresh()
    }

    override fun refreshView() {
        refreshRv?.refreshDataAfterScrollTop()
    }
}