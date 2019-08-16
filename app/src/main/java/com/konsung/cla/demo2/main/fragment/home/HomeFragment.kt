package com.konsung.cla.demo2.main.fragment.home

import androidx.recyclerview.widget.LinearLayoutManager
import com.konsung.basic.bean.BannerData
import com.konsung.basic.bean.HomeData
import com.konsung.basic.ui.BasicFragment
import com.konsung.basic.ui.BasicPresenter
import com.konsung.basic.ui.RefreshRecyclerView
import com.konsung.basic.ui.SpaceDecoration
import com.konsung.basic.util.Debug
import com.konsung.cla.demo2.R
import com.konsung.cla.demo2.presenter.BannerPresenter
import com.konsung.cla.demo2.presenter.HomeDataPresenter
import com.konsung.cla.demo2.presenter.LoadBannerView
import com.konsung.cla.demo2.presenter.LoadHomeView

/**
 * 首页
 */
class HomeFragment : BasicFragment() {

    private val refreshRv by lazy { rootView?.findViewById<RefreshRecyclerView>(R.id.refreshRv) }
    private val homeAdapter = HomeAdapter(mutableListOf())
    private lateinit var headView: BannerHeadView

    private val loadBanner = initLoadBanner()
    private val loadHomeData = initLoadHomeData()
    private var page = 0
    private var isOver = false

    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun getMultiplyId(): Int = R.id.multiplyStatusView

    override fun initView() {
        headView = BannerHeadView(context!!)
        homeAdapter.addHeaderView(headView)
        initRefreshView()
    }

    override fun firstShow() {
        fetchBannerData()
        fetchHomeData()
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

    private fun fetchHomeData() {
        loadHomeData.load(context, page)
    }

    override fun initPresenters(): List<BasicPresenter>? {
        return listOf(loadBanner, loadHomeData)
    }

    private fun initRefreshView() {

        refreshRv?.apply {
            rv.apply {
                val manager = LinearLayoutManager(context)
                val space = context.resources.getDimension(R.dimen.dp_8)
                addItemDecoration(SpaceDecoration(space.toInt(), true))
                adapter = homeAdapter
                layoutManager = manager
            }

            setOnRefreshListener {
                refreshData()
            }

            setOnLoadMoreListener {

                Debug.info(TAG, "HomeFragment initRefreshView isOver?$isOver page?$page")

                if (isOver) {
                    return@setOnLoadMoreListener
                }

                ++page
                fetchHomeData()
            }
        }
    }

    override fun refreshData() {

        Debug.info(TAG, "HomeFragment refreshData")
        if (!headView.loadSuccess) {
            loadBanner.load(context)
        }

        page = 0
        fetchHomeData()
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
                    page = t.curPage
                    isOver = t.isOver

                    myHandler.post {
                        homeAdapter.addData(it)
                    }

                    refreshRv?.apply {
                        finishRefresh()
                        finishLoadMore(2000, true, isOver)
                    }
                }

                myHandler.sendEmptyMessage(SHOW_CONTENT)
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

                myHandler.sendEmptyMessage(SHOW_ERROR)

            }

            override fun noNetwork() {
                myHandler.sendEmptyMessageDelayed(SHOW_NO_NETWORK, 1000)
            }
        })
    }

}