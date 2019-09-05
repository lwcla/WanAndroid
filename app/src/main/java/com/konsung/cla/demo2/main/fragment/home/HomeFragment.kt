package com.konsung.cla.demo2.main.fragment.home

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
import com.konsung.cla.demo2.web.WebViewAty

/**
 * 首页
 */
class HomeFragment : BasicFragment() {

    private val refreshRv by lazy { rootView?.findViewById<RefreshRecyclerView>(R.id.refreshRv) }
    private val homeAdapter by lazy { context?.let { HomeAdapter(it, mutableListOf()) } }
    private lateinit var headView: BannerHeadView

    private val loadBanner = initLoadBanner()
    private val loadHomeData = initLoadHomeData()
    private val collectPresenter = initCollectPresenter()

    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun getMultiplyId(): Int = R.id.multiplyStatusView

    override fun initPresenters(): List<BasicPresenter>? {
        return listOf(loadBanner, loadHomeData, collectPresenter)
    }

    override fun initView() {
        headView = BannerHeadView(context!!)

        homeAdapter?.apply {
            addHeaderView(headView)

            setOnItemClickListener { _, _, position ->
                context?.let {
                    val d = findDataByPosition(position)
                    val cxt = it
                    d?.apply {
                        App.productUtils.startWebAty(cxt, title, link, id, d.collect)
                    }
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

                        val id = findIdByPosition(position)
                        if (id < 0) {
                            toast(TAG, R.string.data_error)
                            return@setOnItemChildClickListener
                        }

                        //先把状态设置为收藏成功，如果收藏失败的话，再改回来
                        val data = homeAdapter?.findDataByPosition(position)
                        var collectFlag = true
                        data?.let {
                            collectFlag = it.collect
                            it.collect = !collectFlag
                            homeAdapter?.notifyItemChanged(position)
                        }

                        collectPresenter.collect(context, position, id, collectFlag)
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
        loadHomeData.loadWithTopData(context)
    }

    override fun show() {
        headView.startPlay()
    }

    override fun pause() {
        //停止轮播
        headView.stopAutoPlay()
    }

    private fun fetchBannerData() {
        loadBanner.load(context)
    }

    private fun initRefreshView() {

        refreshRv?.apply {

            initRecyclerView(homeAdapter, fragmentRefresh, index) {
                refreshRv?.autoRefresh()
                resetHomeData()
            }

            setOnRefreshListener {
                resetHomeData()
            }

            setOnLoadMoreListener {
                loadHomeData.loadMore(context)
            }
        }
    }

    /**
     *刷新数据
     */
    override fun resetHomeData() {

        Debug.info(TAG, "HomeFragment refreshDataAfterScrollTop")
        if (!headView.loadSuccess) {
            loadBanner.load(context)
        }

        loadHomeData.page = 0
        homeAdapter?.data?.clear()
        homeAdapter?.notifyDataSetChanged()
        loadHomeData.loadWithTopData(context)
    }

    /**
     * 刷新数据
     */
    override fun refreshView() {

        val size = homeAdapter?.data?.size ?: 0
        if (size == 0) {
            //如果之前没有获取到数据，那么这个时候就不滚动recyclerView直接获取数据
            refreshRv?.autoRefresh()
            resetHomeData()
            return
        }

        refreshRv?.refreshDataAfterScrollTop()
    }

    private fun initCollectPresenter(): CollectPresenter {

        val view = object : CollectView() {

            override fun success(position: Int, toCollect: Boolean) {
                if (toCollect) {
                    toast(WebViewAty.TAG, R.string.collect_success)
                } else {
                    toast(WebViewAty.TAG, R.string.cancel_collect_success)
                }
            }

            override fun failed(string: String, position: Int, toCollect: Boolean) {
                if (toCollect) {
                    toast(WebViewAty.TAG, R.string.collect_failed)
                } else {
                    toast(WebViewAty.TAG, R.string.cancel_collect_failed)
                }

                val data = homeAdapter?.findDataByPosition(position)
                data?.let {
                    it.collect = !toCollect
                    homeAdapter?.notifyItemChanged(position)
                }
            }
        }

        return CollectPresenter(view)
    }

    private fun initLoadBanner(): BannerPresenter {

        return BannerPresenter(object : LoadBannerView() {
            override fun failed(string: String) {
                context?.let {
                    headView.error()
                }
            }

            override fun success(t: List<BannerData>) {
                context?.let {
                    headView.setData(t, resume)
                }
            }
        })
    }

    private fun initLoadHomeData(): HomeDataPresenter {
        return HomeDataPresenter(object : LoadHomeView() {

            override fun success(t: HomeData) {

                if (context == null) {
                    return
                }

                t.datas?.let {
                    loadHomeData.page = t.curPage
                    loadHomeData.over = t.over

                    myHandler.post {
                        homeAdapter?.addData(it)
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
        })
    }
}