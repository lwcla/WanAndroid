package com.konsung.cla.demo2.main.fragment.home

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.konsung.basic.bean.BannerData
import com.konsung.basic.bean.HomeData
import com.konsung.basic.ui.BasicFragment
import com.konsung.basic.ui.BasicPresenter
import com.konsung.basic.ui.RefreshRecyclerView
import com.konsung.basic.ui.SpaceDecoration
import com.konsung.basic.util.Debug
import com.konsung.basic.util.toast
import com.konsung.cla.demo2.App
import com.konsung.cla.demo2.R

/**
 * 首页
 */
class HomeFragment : BasicFragment() {

    private val refreshRv by lazy { rootView?.findViewById<RefreshRecyclerView>(R.id.refreshRv) }
    private val homeAdapter by lazy { context?.let { HomeAdapter(it, mutableListOf()) } }
    private lateinit var headView: BannerHeadView

    private val loadBanner = initLoadBanner()
    private val loadHomeData = initLoadHomeData()
    private var page = 0
    private var isOver = false
    private var needRefresh = false

    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun getMultiplyId(): Int = R.id.multiplyStatusView

    override fun initView() {
        headView = BannerHeadView(context!!)

        homeAdapter?.apply {
            addHeaderView(headView)

            setOnItemClickListener { adapter, view, position ->
                context?.let {
                    val d = homeAdapter?.findDataByPosition(position)
                    val cxt = it
                    d?.apply {
                        App.productUtils.startWebAty(cxt, title, link, chapterId)
                    }
                }
            }

            setOnItemChildClickListener { adapter, view, position ->
                when (view.id) {
                    R.id.imvEnvelopePic -> {

                        val url = homeAdapter?.findImageByPosition(position)
                        url?.let {
                            toast(TAG, "图片地址：$url")
                        }
                    }

                    R.id.imvStart -> toast(TAG, "点击收藏 $position")
                    R.id.llNice -> toast(TAG, "点击赞 $position")
                }
            }
        }

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
                homeAdapter?.setHasStableIds(true)
                itemAnimator?.changeDuration = 0
                adapter = homeAdapter
                layoutManager = manager
                addOnScrollListener(object : RecyclerView.OnScrollListener() {

                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                        Debug.info(TAG, "HomeFragment onScrollStateChanged newState=$newState")

                        //newState ==0 时表示滚动停止
                        if (newState != 0) {
                            return
                        }


                        //recyclerView.canScrollVertically(-1)为false时表示滚动到顶部
                        if (recyclerView.canScrollVertically(-1)) {
                            return
                        }

                        fragmentRefresh?.refresh(false, index)

                        if (needRefresh) {
                            needRefresh = false
                            refreshRv?.autoRefresh()
                            refreshData()
                        }
                    }

                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                        Debug.info(TAG, "HomeFragment onScrolled dy=$dy")
                        if (dy > 0) {
                            //向下滚动
                            fragmentRefresh?.refresh(true, index)
                        }
                    }
                })
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
        homeAdapter?.data?.clear()
        fetchHomeData()
    }

    override fun refreshView() {

        val size = homeAdapter?.data?.size ?: 0
        if (size == 0) {
            refreshRv?.autoRefresh()
            refreshData()
            return
        }

        needRefresh = true
        refreshRv?.scrollToTop()
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
                        homeAdapter?.addData(it)
                    }

                    refreshRv?.apply {
                        finishRefresh()
                        finishLoadMore(200, true, isOver)
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

                if (homeAdapter?.data?.isEmpty() == null) {
                    myHandler.sendEmptyMessage(SHOW_ERROR)
                }
            }

            override fun noNetwork() {
                if (homeAdapter?.data?.isEmpty() == null) {
                    myHandler.sendEmptyMessageDelayed(SHOW_NO_NETWORK, 1000)
                }
            }
        })
    }
}