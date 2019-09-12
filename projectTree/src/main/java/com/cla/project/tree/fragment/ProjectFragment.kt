package com.cla.project.tree.fragment

import android.content.Context
import android.widget.TextView
import com.cla.project.tree.ProjectAdapter
import com.cla.project.tree.R
import com.cla.project.tree.presenter.ProjectPresenter
import com.cla.project.tree.presenter.ProjectView
import com.konsung.basic.bean.HomeData
import com.konsung.basic.bean.project.ProjectBean
import com.konsung.basic.presenter.CollectPresenter
import com.konsung.basic.presenter.CollectView
import com.konsung.basic.ui.BasicFragment
import com.konsung.basic.ui.BasicPresenter
import com.konsung.basic.ui.RefreshRecyclerView
import com.konsung.basic.util.AppUtils
import com.konsung.basic.util.toast

abstract class ProjectFragment : BasicFragment() {

    private val refreshRv: RefreshRecyclerView? by lazy { showView?.findViewById<RefreshRecyclerView>(R.id.refreshRv) }
    private var adapter: ProjectAdapter? = null

    private var over = false
    private val projectView = object : ProjectView() {

        override fun success(t: ProjectBean, refreshData: Boolean) {
            over = t.over
            presenter.page = t.curPage

            refreshRv?.finishRefresh(200)
            refreshRv?.finishLoadMore(200, true, over)

            val list = mutableListOf<HomeData.DatasBean>()
            list.addAll(t.datas!!.filterNotNull())

            if (refreshData) {
                refreshData(list)
            } else {
                loadMore(list)
            }
        }

        override fun failed(string: String) {
            refreshRv?.finishRefresh(false)
            refreshRv?.finishLoadMore(false)
        }
    }
    private val presenter: ProjectPresenter by lazy { initProjectPresenter(projectView) }
    private val collectPresenter by lazy { initCollectPresenter() }

    override fun getLayoutId(): Int = R.layout.fragment_project

    override fun initPresenters(): List<BasicPresenter>? = listOf(presenter)

    override fun initView() {
        adapter = ProjectAdapter(context!!, listOf())
        refreshRv?.initRecyclerView(adapter, fragmentRefresh, index) {
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
                presenter.loadMore()
            }
        }

        adapter?.apply {

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

                    //点击图片
                    R.id.imvEnvelope -> {
                        val url = findImageByPosition(position)
                        if (url.isNullOrEmpty()) {
                            toast(TAG, R.string.image_url_is_null)
                            return@setOnItemChildClickListener
                        }

                        AppUtils.startScreenImageAty(context!!, url)
                    }

                    //收藏
                    R.id.imvStart -> {

                        val data = adapter?.findDataByPosition(position)
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
                            adapter?.refreshCollectStatus(position, data)
                        }
                    }
                }
            }

        }
    }

    override fun initData() {
        resetData()
    }

    private fun refreshData(t: List<HomeData.DatasBean>) {
        adapter?.setNewData(t)
    }

    private fun loadMore(t: List<HomeData.DatasBean>) {
        adapter?.addData(t)
    }


    private fun initCollectPresenter(): CollectPresenter {

        val view = object : CollectView() {

            override fun failed(context: Context, string: String, position: Int, toCollect: Boolean) {
                adapter?.refreshCollectStatus(position, !toCollect)
            }
        }

        return CollectPresenter(this, view)
    }

    override fun resetData() {
        presenter.refresh()
    }

    override fun refreshView() {
        refreshRv?.refreshDataAfterScrollTop()
    }

    abstract fun initProjectPresenter(view: ProjectView): ProjectPresenter
}