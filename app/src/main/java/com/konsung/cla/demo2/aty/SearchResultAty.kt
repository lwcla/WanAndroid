package com.konsung.cla.demo2.aty

import android.content.Context
import android.os.Bundle
import android.transition.Fade
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.classic.common.MultipleStatusView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.konsung.basic.bean.HomeData
import com.konsung.basic.bean.project.ProjectBean
import com.konsung.basic.config.BaseConfig
import com.konsung.basic.presenter.CollectPresenter
import com.konsung.basic.presenter.CollectView
import com.konsung.basic.receiver.CollectReceiver
import com.konsung.basic.receiver.CollectResult
import com.konsung.basic.ui.BasicAty
import com.konsung.basic.ui.BasicPresenter
import com.konsung.basic.ui.RefreshRecyclerView
import com.konsung.basic.util.AppUtils
import com.konsung.basic.util.Debug
import com.konsung.basic.util.toast
import com.konsung.cla.demo2.R
import com.konsung.cla.demo2.adapter.SearchResultAdapter
import com.konsung.cla.demo2.presenter.SearchResultPresenter
import com.konsung.cla.demo2.presenter.SearchResultView
import kotlinx.android.synthetic.main.activity_search_result.*

class SearchResultAty : BasicAty(), CollectResult {


    companion object {
        val TAG: String = SearchResultAty::class.java.simpleName
        const val INIT_DELAY = 500L
    }

    private var multiplyStatusView: MultipleStatusView? = null
    private var refreshRv: RefreshRecyclerView? = null

    private var searchResultAdapter: SearchResultAdapter? = null

    private val searchResultPresenter by lazy { initSearchResultPresenter() }
    private val collectPresenter by lazy { initCollectPresenter() }

    private var key: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.enterTransition = Fade().setDuration(INIT_DELAY)
        window.exitTransition = Fade().setDuration(INIT_DELAY)
        CollectReceiver.registerObserver(this)
    }

    override fun getLayoutId(): Int = R.layout.activity_search_result

    override fun initPresenter(): List<BasicPresenter>? = listOf(searchResultPresenter, collectPresenter)

    override fun initView() {

        val title = getKey()
        if (title.isNullOrEmpty()) {
            toast(TAG, R.string.key_is_null)
            finish()
            return
        }

        toolbar.title = title
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        multiplyStatusView = findViewById<MultipleStatusView>(R.id.multiplyStatusView)
        multiplyStatusView?.showLoading()
    }

    override fun initEvent() {
        toolbar.setNavigationOnClickListener { finish() }
        multiplyStatusView?.setOnRetryClickListener {
            showLoadView()
            resetData()
        }
        fab.setOnClickListener {
            refreshRv?.refreshDataAfterScrollTop()
        }
    }

    override fun initData() {
        resetData()
    }

    private fun getKey(): String? {

        if (key.isNullOrEmpty()) {
            key = intent.getStringExtra(BaseConfig.SEARCH_KEY)
        }

        return key
    }

    private fun setSearchResult(t: List<HomeData.DatasBean>) {

        if (searchResultAdapter == null) {
            refreshRv = multiplyStatusView?.findViewById(R.id.refreshRv)

            searchResultAdapter = SearchResultAdapter(context, t)
            searchResultAdapter?.apply {

                setOnItemClickListener { _, view, position ->
                    val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
                    val d = findDataByPosition(position)
                    d?.apply {
                        AppUtils.startWebAty(this@SearchResultAty, context, tvTitle, title, link, artId = id, dataPosition = position, collect = d.collect, needCollect = true)
                    }
                }

                setOnItemChildClickListener { _, view, position ->

                    when (view.id) {
                        //收藏
                        R.id.imvStart -> {

                            val data = searchResultAdapter?.findDataByPosition(position)
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
                                searchResultAdapter?.refreshCollectStatus(position, data)
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
                    searchResultPresenter.loadMore()
                }
            }

            refreshRv?.rv?.let {
                fab.attachToRecyclerView(it)
            }

            refreshRv?.initRecyclerView(searchResultAdapter, null, 0, false) {
                refreshRv?.autoRefresh()
                resetData()
            }
        } else {
            searchResultAdapter?.setNewData(t)
        }
    }

    private fun initSearchResultPresenter(): SearchResultPresenter {

        val view = object : SearchResultView() {

            override fun success(t: ProjectBean, refreshData: Boolean) {

                val list = mutableListOf<HomeData.DatasBean>()
                t.datas?.let {
                    list.addAll(it.filterNotNull())
                }

                if (list.isEmpty() && searchResultAdapter?.data?.size ?: 0 == 0) {
                    multiplyStatusView?.showEmpty()
                    return
                }

                searchResultPresenter.page = t.curPage

                refreshRv?.finishLoadMore(0, true, t.over)
                refreshRv?.finishRefresh()

                if (refreshData) {
                    setSearchResult(list)
                } else {
                    searchResultAdapter?.addData(list)
                }
            }

            override fun failed(string: String) {
                refreshRv?.finishLoadMore(false)
                refreshRv?.finishRefresh(false)
            }

            override fun noNetwork() {
                refreshRv?.finishLoadMore(false)
                refreshRv?.finishRefresh(false)
            }
        }

        return SearchResultPresenter(this, view, getKey())
    }

    private fun initCollectPresenter(): CollectPresenter {

        val view = object : CollectView() {

            override fun failed(context: Context, string: String, position: Int, toCollect: Boolean) {
                //收藏失败
                searchResultAdapter?.refreshCollectStatus(position, !toCollect)
            }
        }

        return CollectPresenter(this, view)
    }


    private fun resetData() {
        searchResultPresenter.refresh()
    }

    /**
     * 从WebView点击收藏的结果
     */
    override fun collectResult(success: Boolean, collectId: Int, position: Int, toCollect: Boolean) {
        Debug.info(TAG, "collectResult success?$success collectId=$collectId position=$position toCollect?$toCollect")

        if (!success) {
            return
        }

        val data = searchResultAdapter?.findDataByPosition(position) ?: return

        if (data.id != collectId) {
            return
        }

        searchResultAdapter?.refreshCollectStatus(position, toCollect)
    }

    override fun showLoadView() {
        multiplyStatusView?.showLoading()
    }

    override fun showContentView() {
        multiplyStatusView?.showContent()
    }

    override fun showErrorView() {
        multiplyStatusView?.showError()
    }

    override fun showNoNetworkView() {
        multiplyStatusView?.showNoNetwork()
    }
}
