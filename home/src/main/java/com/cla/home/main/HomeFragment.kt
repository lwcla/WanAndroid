package com.cla.home.main

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseViewHolder
import com.cla.home.R
import com.konsung.basic.adapter.BasicDataQuickAdapter
import com.konsung.basic.bean.BannerData
import com.konsung.basic.ui.BasicPresenter
import com.konsung.basic.ui.HomeDataFragment
import com.konsung.basic.util.AppUtils

/**
 * 首页
 */
class HomeFragment : HomeDataFragment() {

    init {
        needDelayInitView = false
    }

    private lateinit var headView: BannerHeadView

    private val loadBanner by lazy { initLoadBanner() }
    private val loadHomeData by lazy { HomeDataPresenter(this, homeView) }

    var homeIndex = 0

    override fun initPresenters(): List<BasicPresenter>? {
        return listOf(loadBanner, loadHomeData, collectPresenter)
    }

    override fun initView() {
        super.initView()
        headView = BannerHeadView(context!!)
        dataAdapter?.addHeaderView(headView)
    }

    override fun initEvent() {
        super.initEvent()
        headView.onBannerItemClickListener = object : BannerHeadView.OnBannerItemClickListener {
            override fun click(data: BannerData) {
                context?.let {
                    AppUtils.startWebAty(it, title = data.title, link = data.url, artId = data.id, dataPosition = 0, collect = false, needCollect = false)
                }
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

    override fun initDataAdapter(): BasicDataQuickAdapter<BaseViewHolder>? = context?.let { HomeAdapter(it, mutableListOf()) }


    override fun show() {
        headView.startPlay()
    }

    override fun leave() {
        //停止轮播
        headView.stopAutoPlay()
    }


    override fun loadMoreData() {
        loadHomeData.loadMore()
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

    /**
     *刷新数据
     */
    override fun resetData() {
        if (!headView.loadSuccess) {
            loadBanner.load()
        }

        loadHomeData.loadWithTopData()
    }

    override fun getScrollIndex(): Int = homeIndex

    override fun getImvEnvelopePicId(): Int = R.id.imvEnvelopePic

    override fun getImvStartId(): Int = R.id.imvStart
}