package com.konsung.basic.ui

import android.widget.TextView
import com.chad.library.adapter.base.BaseViewHolder
import com.konsung.basic.adapter.BasicDataQuickAdapter
import com.konsung.basic.util.AppUtils
import com.konsung.basic.util.R
import com.konsung.basic.util.toast

abstract class HomeDataFragment : BasicFragment() {

    protected var dataAdapter: BasicDataQuickAdapter<BaseViewHolder>? = null

    override fun getLayoutId(): Int = R.layout.view_fresh_rv

    override fun initView() {
        refreshRv = showView?.findViewById(R.id.refreshRv)
        dataAdapter = initDataAdapter()
        dataListAdapterHelper = dataAdapter

        refreshRv?.initRecyclerView(dataAdapter, fragmentRefresh, getScrollIndex(), true) {
            refreshRv?.autoRefresh()
            resetData()
        }
    }

    override fun initEvent() {
        dataAdapter?.apply {
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
                    getImvEnvelopePicId() -> {
                        val url = findImageByPosition(position)
                        if (url.isNullOrEmpty()) {
                            toast(TAG, R.string.image_url_is_null)
                            return@setOnItemChildClickListener
                        }

                        AppUtils.startScreenImageAty(activity, url)
                    }

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

                        val b = collectPresenter.collect(position, id, data.collect)
                        if (b) {
                            dataAdapter?.refreshCollectStatus(position, data)
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
                loadMoreData()
            }
        }
    }

    override fun initData() {

    }

    open fun getScrollIndex() = index

    /**
     * 开发信息中图片的id
     */
    open fun getImvEnvelopePicId(): Int = -100

    /**
     * 收藏按钮的id
     */
    open fun getImvStartId(): Int = -200

    abstract fun initDataAdapter(): BasicDataQuickAdapter<BaseViewHolder>?

    abstract fun loadMoreData()
}