package com.konsung.cla.demo2.main.fragment.home

import android.content.Context
import android.widget.TextView
import com.konsung.basic.bean.BannerData
import com.konsung.basic.bean.HomeData
import com.konsung.basic.presenter.CollectPresenter
import com.konsung.basic.presenter.CollectView
import com.konsung.basic.ui.BasicFragment
import com.konsung.basic.ui.BasicPresenter
import com.konsung.basic.util.Debug
import com.konsung.basic.util.toast
import com.konsung.cla.demo2.App
import com.konsung.cla.demo2.R

/**
 * 首页
 */
class HomeFragment : BasicFragment() {

    init {
        needDelayInitView = false
    }

    private val homeAdapter by lazy { context?.let { HomeAdapter(it, mutableListOf()) } }
    private lateinit var headView: BannerHeadView

    private val loadBanner by lazy { initLoadBanner() }
    private val loadHomeData by lazy { initLoadHomeData() }
    private val collectPresenter by lazy { initCollectPresenter() }

    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun initPresenters(): List<BasicPresenter>? {
        return listOf(loadBanner, loadHomeData, collectPresenter)
    }

    override fun initView() {
        refreshRv = showView?.findViewById(R.id.refreshRv)
        headView = BannerHeadView(context!!)
        homeAdapter?.addHeaderView(headView)
        refreshRv?.initRecyclerView(homeAdapter, fragmentRefresh, index, true) {
            refreshRv?.autoRefresh()
            resetData()
        }
    }

    override fun initEvent() {

        headView.onBannerItemClickListener = object : BannerHeadView.OnBannerItemClickListener {
            override fun click(data: BannerData) {
                App.productUtils.startWebAty(context, title = data.title, link = data.url, artId = data.id, dataPosition = 0, collect = false, needCollect = false)
            }
        }

        homeAdapter?.apply {
            setOnItemClickListener { _, view, position ->
                val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
                val d = findDataByPosition(position)
                d?.apply {
                    App.productUtils.startWebAty(activity, context, tvTitle, title, link, artId = id, dataPosition = position, collect = d.collect, needCollect = true)
                }
            }

            setOnItemChildClickListener { _, view, position ->

                if (context == null) {
                    return@setOnItemChildClickListener
                }

                when (view.id) {

                    //点击图片
                    R.id.imvEnvelopePic -> {
                        val url = findImageByPosition(position)
                        if (url.isNullOrEmpty()) {
                            toast(TAG, R.string.image_url_is_null)
                            return@setOnItemChildClickListener
                        }

                        App.productUtils.startScreenImageAty(context!!, url)
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

        refreshRv?.apply {

            setOnRefreshListener {
                resetData()
            }

            setOnLoadMoreListener {
                loadHomeData.loadMore()
            }
        }
    }

    override fun initData() {
        loadBanner.load()
        loadHomeData.loadWithTopData()
    }

    override fun show() {
        headView.startPlay()
    }

    override fun leave() {
        //停止轮播
        headView.stopAutoPlay()
    }

    private fun initCollectPresenter(): CollectPresenter {

        val view = object : CollectView() {

            override fun failed(context: Context, string: String, position: Int, toCollect: Boolean) {
                homeAdapter?.refreshCollectStatus(position, !toCollect)
            }
        }

        return CollectPresenter(this, view)
    }

    private fun initLoadBanner(): BannerPresenter {

        val view = object : LoadBannerView() {

            override fun success(t: List<BannerData>, refreshData: Boolean) {
                headView.setData(t, resume)
            }

            override fun failed(string: String) {
                headView.error()
            }
        }

        return BannerPresenter(this, view)
    }

    private fun initLoadHomeData(): HomeDataPresenter {

        val view = object : LoadHomeView() {

            override fun success(context: Context, t: HomeData, refreshData: Boolean) {

                t.datas?.let {
                    loadHomeData.page = t.curPage
                    loadHomeData.over = t.over

                    myHandler.post {
                        if (refreshData) {
                            homeAdapter?.setNewData(it)
                        } else {
                            homeAdapter?.addData(it)
                        }
                    }

                    refreshRv?.apply {
                        finishRefresh(300)
                        finishLoadMore(200, true, t.over)
                    }
                }
            }
        }

        return HomeDataPresenter(this, view)
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