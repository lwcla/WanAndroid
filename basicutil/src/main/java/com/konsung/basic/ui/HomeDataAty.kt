package com.konsung.basic.ui

import android.content.Context
import android.os.Bundle
import android.transition.Fade
import android.widget.TextView
import androidx.annotation.IdRes
import com.chad.library.adapter.base.BaseViewHolder
import com.konsung.basic.adapter.BasicDataQuickAdapter
import com.konsung.basic.bean.HomeData
import com.konsung.basic.presenter.CollectPresenter
import com.konsung.basic.presenter.CollectView
import com.konsung.basic.receiver.CollectReceiver
import com.konsung.basic.receiver.CollectResult
import com.konsung.basic.util.AppUtils
import com.konsung.basic.util.Debug
import com.konsung.basic.util.R
import com.konsung.basic.util.toast
import com.konsung.basic.view.MultipleStatusView
import kotlinx.android.synthetic.main.activity_home_data.*

abstract class HomeDataAty : BasicAty(), CollectResult {

    companion object {
        const val INIT_DELAY = 500L
    }

    private val multiplyStatusView by lazy { findViewById<MultipleStatusView>(R.id.multiplyStatusView) }
    protected val refreshRv by lazy { multiplyStatusView.findViewById<RefreshRecyclerView>(R.id.refreshRv) }
    protected var dataAdapter: BasicDataQuickAdapter<BaseViewHolder>? = null

    protected val homeView = object : HomeView() {

        override fun success(t: HomeData, refreshData: Boolean) {

            if (t.beanList.isEmpty() && dataAdapter?.data?.size ?: 0 == 0) {
                multiplyStatusView.showEmpty()
                return
            }

            if (refreshData) {
//                setSearchResult(t.beanList)
                setAdapter(t.beanList)
            } else {
                dataAdapter?.addData(t.beanList)
            }
        }

        override fun complete(success: Boolean) {
            if (!success) {
                refreshRv.finishLoadMore(false)
                refreshRv.finishRefresh(false)
            }
        }

        override fun getRefreshRv(): RefreshRecyclerView? = refreshRv
    }

    protected val collectPresenter by lazy { initCollectPresenter() }

    protected var refreshAfterScrollTop = true
    protected var isCollectListPage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.enterTransition = Fade().setDuration(INIT_DELAY)
        window.exitTransition = Fade().setDuration(INIT_DELAY)
        CollectReceiver.registerObserver(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        CollectReceiver.unRegisterObserver(this)
    }

    override fun getLayoutId(): Int = R.layout.activity_home_data

    override fun initView() {
        toolbar.title = getAtyTitle()
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        showLoadView()
    }

    override fun initEvent() {
        toolbar.setNavigationOnClickListener { finish() }
        multiplyStatusView.setOnRetryClickListener {
            multiplyStatusView.showLoading()
            resetData()
        }
        fab.setOnClickListener {
            refreshRv.refreshDataAfterScrollTop()
        }
    }

    override fun initData() {
        resetData()
    }

    private fun initCollectPresenter(): CollectPresenter {

        val view = object : CollectView() {

            override fun failed(context: Context, string: String, position: Int, toCollect: Boolean) {
                //收藏失败
                dataAdapter?.refreshCollectStatus(position, !toCollect)
            }
        }

        return CollectPresenter(this, view, isCollectListPage)
    }

    private fun setAdapter(t: List<HomeData.DatasBean>) {
        if (dataAdapter == null) {
            dataAdapter = initAdapter(t)
            dataAdapter?.apply {

                setOnItemClickListener { _, view, position ->
                    val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
                    val d = findDataByPosition(position)
                    d?.apply {
                        AppUtils.startWebAty(this@HomeDataAty, context, tvTitle, title, link, artId = id, dataPosition = position, collect = d.collect, needCollect = true)
                    }
                }

                setOnItemChildClickListener { _, view, position ->

                    when (view.id) {
                        //收藏
                        getImvStartId() -> {

                            val data = dataAdapter?.findDataByPosition(position)
                            if (data == null) {
                                toast(TAG, R.string.data_error)
                                return@setOnItemChildClickListener
                            }

                            val id = data.id
                            if (id < 0) {
                                toast(TAG, R.string.data_error)
                                return@setOnItemChildClickListener
                            }

                            val b = collectPresenter.collect(position, id, data.originId, collect = data.collect)
                            if (b) {
                                dataAdapter?.refreshCollectStatus(position, data)
                            }
                        }
                    }
                }
            }

            refreshRv.apply {

                setOnRefreshListener {
                    resetData()
                }

                setOnLoadMoreListener {
                    loadMoreData()
                }
            }

            refreshRv.rv.let {
                fab.attachToRecyclerView(it)
            }

            refreshRv.refreshAfterScrollTop = refreshAfterScrollTop
            refreshRv.initRecyclerView(dataAdapter, null, 0, false) {
                refreshRv.autoRefresh()
                resetData()
            }
        } else {
            dataAdapter?.setNewData(t)
        }
    }

    /**
     * 从WebView点击收藏的结果
     */
    override fun collectResult(success: Boolean, collectId: Int, position: Int, toCollect: Boolean) {
        Debug.info(TAG, "collectResult success?$success collectId=$collectId position=$position toCollect?$toCollect")

        if (!success) {
            return
        }

        val data = dataAdapter?.findDataByPosition(position) ?: return

        if (data.id != collectId) {
            return
        }

        dataAdapter?.refreshCollectStatus(position, toCollect)
    }

    override fun showContentView() {
        multiplyStatusView.showContent()
    }

    override fun showErrorView() {
        multiplyStatusView.showError()
    }

    override fun showLoadView() {
        multiplyStatusView.showLoading()
    }

    override fun showNoNetworkView() {
        multiplyStatusView.showNoNetwork()
    }

    override fun showEmptyView() {
        multiplyStatusView.showEmpty()
    }

    abstract fun getAtyTitle(): String

    @IdRes
    abstract fun getImvStartId(): Int

    abstract fun initAdapter(t: List<HomeData.DatasBean>): BasicDataQuickAdapter<BaseViewHolder>

    abstract fun loadMoreData()

    abstract fun resetData()
}