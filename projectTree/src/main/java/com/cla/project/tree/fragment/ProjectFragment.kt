package com.cla.project.tree.fragment

import android.widget.TextView
import com.cla.project.tree.ProjectAdapter
import com.cla.project.tree.R
import com.konsung.basic.bean.HomeData
import com.konsung.basic.ui.BasicFragment
import com.konsung.basic.ui.BasicPresenter
import com.konsung.basic.util.AppUtils
import com.konsung.basic.util.toast

abstract class ProjectFragment : BasicFragment() {

    private var adapter: ProjectAdapter? = null

    private val projectView = object : ProjectView() {

        override fun success(t: HomeData, refreshData: Boolean) {
            presenter.page = t.curPage

            fetSuccess(t.over)

            val list = mutableListOf<HomeData.DatasBean>()
            t.datas?.let {
                list.addAll(it)
            }

            if (refreshData) {
                refreshData(list)
            } else {
                loadMore(list)
            }
        }
    }

    private val presenter: ProjectPresenter by lazy { initProjectPresenter(projectView) }

    override fun getLayoutId(): Int = R.layout.view_fresh_rv

    override fun initPresenters(): List<BasicPresenter>? = listOf(presenter, collectPresenter)

    override fun initView() {
        refreshRv = showView?.findViewById(R.id.refreshRv)
        adapter = ProjectAdapter(context!!, listOf())
        dataListAdapterHelper = adapter

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

                if (activity == null) {
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

                        AppUtils.startScreenImageAty(activity, url)
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

    override fun resetData() {
        presenter.refresh()
    }

    abstract fun initProjectPresenter(view: ProjectView): ProjectPresenter
}