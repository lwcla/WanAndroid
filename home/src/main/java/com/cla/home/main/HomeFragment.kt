package com.cla.home.main

import android.content.Context
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cla.home.R
import com.konsung.basic.bean.BannerData
import com.konsung.basic.bean.HomeData
import com.konsung.basic.ui.BasicFragment
import com.konsung.basic.ui.BasicPresenter
import com.konsung.basic.util.AppUtils
import com.konsung.basic.util.toast

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

    var homeIndex = 0

    override fun getLayoutId(): Int = R.layout.view_fresh_rv

    override fun initPresenters(): List<BasicPresenter>? {
        return listOf(loadBanner, loadHomeData, collectPresenter)
    }

    override fun initView() {
        refreshRv = showView?.findViewById(R.id.refreshRv)
        headView = BannerHeadView(context!!)

        homeAdapter?.addHeaderView(headView)
        dataListAdapterHelper = homeAdapter

        refreshRv?.initRecyclerView(homeAdapter, fragmentRefresh, homeIndex, true) {
            refreshRv?.autoRefresh()
            resetData()
        }
    }

    override fun initEvent() {

        headView.onBannerItemClickListener = object : BannerHeadView.OnBannerItemClickListener {
            override fun click(data: BannerData) {
                context?.let {
                    AppUtils.startWebAty(it, title = data.title, link = data.url, artId = data.id, dataPosition = 0, collect = false, needCollect = false)
                }
            }
        }

        homeAdapter?.apply {
            setOnItemClickListener { _, view, position ->
                val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
                val d = findDataByPosition(position)
                d?.apply {
                    AppUtils.startWebAty(activity, context, tvTitle, title, link, artId = id, dataPosition = position, collect = d.collect, needCollect = true)
                }
            }

            setOnItemChildClickListener { _, view, position ->

                if (activity == null) {
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

                        AppUtils.startScreenImageAty(activity, url)
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

        refreshRv?.rv?.apply {
            addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

                    //newState ==0 时表示滚动停止
                    if (newState != 0) {
                        return
                    }

                    val position = (layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition()
                            ?: -1

//                    Debug.info(TAG,"onScrollStateChanged position=$position")

                    if (position < 0) {
                        return
                    }

                    //banner是否显示
                    if (position == 0) {
                        headView.startPlay()
                    } else {
                        headView.stopAutoPlay()
                    }
                }
            })
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

                    fetSuccess(t.over)
                }
            }
        }

        return HomeDataPresenter(this, view)
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
}