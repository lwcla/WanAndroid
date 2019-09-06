package com.konsung.cla.demo2.main.fragment.home

import android.content.Context
import com.konsung.basic.bean.BannerData
import com.konsung.basic.bean.HomeData
import com.konsung.basic.ui.BasicFragment
import com.konsung.basic.ui.BasicPresenter
import com.konsung.basic.ui.RefreshRecyclerView
import com.konsung.basic.util.Debug
import com.konsung.basic.util.toast
import com.konsung.cla.demo2.App
import com.konsung.cla.demo2.R
import com.konsung.cla.demo2.web.CollectPresenter
import com.konsung.cla.demo2.web.CollectView

/**
 * 首页
 */
class HomeFragment : BasicFragment() {

    private val refreshRv by lazy { rootView?.findViewById<RefreshRecyclerView>(R.id.refreshRv) }
    private val homeAdapter by lazy { context?.let { HomeAdapter(it, mutableListOf()) } }
    private lateinit var headView: BannerHeadView

    private val loadBanner by lazy { initLoadBanner() }
    private val loadHomeData by lazy { initLoadHomeData() }
    private val collectPresenter by lazy { initCollectPresenter() }

    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun getMultiplyId(): Int = R.id.multiplyStatusView

    override fun initPresenters(): List<BasicPresenter>? {
        return listOf(loadBanner, loadHomeData, collectPresenter)
    }

    override fun initView() {
        headView = BannerHeadView(context!!)
        headView.onBannerItemClickListener = object : BannerHeadView.OnBannerItemClickListener {
            override fun click(data: BannerData) {
                App.productUtils.startWebAty(context, title = data.title, link = data.url, artId = data.id, dataPosition = 0, collect = false, needCollect = false)
            }
        }

        homeAdapter?.apply {
            addHeaderView(headView)

            setOnItemClickListener { _, _, position ->
                val d = findDataByPosition(position)
                d?.apply {
                    App.productUtils.startWebAty(context, title, link, artId = id, dataPosition = position, collect = d.collect)
                }
            }

            setOnItemChildClickListener { _, view, position ->
                when (view.id) {

                    //点击图片
                    R.id.imvEnvelopePic -> {

                        val url = findImageByPosition(position)
                        url?.let {
                            toast(TAG, "图片地址：$url")
                        }
                    }

                    //收藏
                    R.id.imvStart -> {

                        val data = homeAdapter?.findDataByPosition(position)
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
                            homeAdapter?.refreshCollectStatus(position, data)
                        }
                    }

                    //点赞
                    R.id.llNice -> toast(TAG, "点击赞 $position")
                }
            }
        }

        initRefreshView()
    }

    override fun firstShow() {
        fetchBannerData()
        loadHomeData.loadWithTopData()
    }

    override fun show() {
        headView.startPlay()
    }

    override fun pause() {
        //停止轮播
        headView.stopAutoPlay()
    }

    private fun fetchBannerData() {
        loadBanner.load()
    }

    private fun initRefreshView() {

        refreshRv?.apply {

            initRecyclerView(homeAdapter, fragmentRefresh, index) {
                refreshRv?.autoRefresh()
                resetData()
            }

            setOnRefreshListener {
                resetData()
            }

            setOnLoadMoreListener {
                loadHomeData.loadMore()
            }
        }
    }

    private fun initCollectPresenter(): CollectPresenter {

        val view = object : CollectView() {

            override fun failed(context: Context, string: String, position: Int, toCollect: Boolean) {
                homeAdapter?.refreshCollectStatus(position, !toCollect)
            }
        }

        return CollectPresenter(context, view)
    }

    private fun initLoadBanner(): BannerPresenter {

        return object : BannerPresenter(context, LoadBannerView()) {
            override fun failed(context: Context, message: String) {
                headView.error()
            }

            override fun success(context: Context, t: List<BannerData>) {
                headView.setData(t, resume)
            }
        }
    }

    private fun initLoadHomeData(): HomeDataPresenter {

        val view = object : LoadHomeView() {

            override fun success(context: Context, t: HomeData, refreshAll: Boolean) {

                t.datas?.let {
                    loadHomeData.page = t.curPage
                    loadHomeData.over = t.over

                    myHandler.post {
                        if (refreshAll) {
                            homeAdapter?.setNewData(it)
                        } else {
                            homeAdapter?.addData(it)
                        }
                    }

                    refreshRv?.apply {
                        finishRefresh()
                        finishLoadMore(200, true, t.over)
                    }
                }

                showContentView()
            }

            override fun failed(string: String) {

                if (context == null) {
                    return
                }

                Debug.info(TAG, "HomeFragment failed info=$string")
                refreshRv?.apply {
                    finishRefresh()
                    finishLoadMore(0, false, false)
                }

                if (homeAdapter?.data?.isEmpty() == null) {
                    showErrorView()
                }
            }

            override fun noNetwork() {
                if (homeAdapter?.data?.isEmpty() == null) {
                    showNoNetworkView()
                }
            }
        }

        return HomeDataPresenter(context, view)
    }

    override fun collectResult(success: Boolean, collectId: Int, position: Int, toCollect: Boolean) {
        Debug.info(TAG, "HomeFragment collectResult success?$success collectId=$collectId position=$position toCollect?$toCollect")

        if (context == null) {
            return
        }

        if (!success) {
            return
        }

        val data = homeAdapter?.findDataByPosition(position) ?: return

        if (data.id != collectId) {
            return
        }

        homeAdapter?.refreshCollectStatus(position, toCollect)
    }

    /**
     *刷新数据
     */
    override fun resetData() {
        if (!headView.loadSuccess) {
            loadBanner.load()
        }

        loadHomeData.loadWithTopData()
    }

    /**
     * 刷新数据
     */
    override fun refreshView() {

        val size = homeAdapter?.data?.size ?: 0
        if (size == 0) {
            //如果之前没有获取到数据，那么这个时候就不滚动recyclerView直接获取数据
            refreshRv?.autoRefresh()
            resetData()
            return
        }

        refreshRv?.refreshDataAfterScrollTop()
    }
}