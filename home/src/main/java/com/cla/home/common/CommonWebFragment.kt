package com.cla.home.common

import android.annotation.SuppressLint
import android.view.Gravity
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.cla.home.R
import com.konsung.basic.bean.CommonWebBean
import com.konsung.basic.ui.BasicFragment
import com.konsung.basic.ui.BasicPresenter
import com.konsung.basic.util.AppUtils

/**
 * 常用网站
 */
class CommonWebFragment : BasicFragment() {


    private var rvCommon: RecyclerView? = null
    private var commonWebAdapter: CommonWebAdapter? = null

    private val commonPresenter by lazy { initCommonWebPresenter() }

    override fun getLayoutId(): Int = R.layout.fragment_common_web

    override fun initPresenters(): List<BasicPresenter>? = listOf(commonPresenter)

    override fun initView() {
        rvCommon = showView?.findViewById(R.id.rvCommon)
    }

    override fun initEvent() {

    }

    override fun initData() {
        commonPresenter.load()
    }

    override fun resetData() {
        commonPresenter.load()
    }

    @SuppressLint("RtlHardcoded")
    fun setAdapter(t: List<CommonWebBean>) {

        if (commonWebAdapter == null) {
            commonWebAdapter = CommonWebAdapter(t)
            commonWebAdapter!!.setOnItemChildClickListener { _, view, position ->

                if (context == null || activity == null) {
                    return@setOnItemChildClickListener
                }

                if (position >= t.size) {
                    return@setOnItemChildClickListener
                }

                val bean = t[position]

                when (view.id) {
                    R.id.tvText -> {
                        AppUtils.startWebAty(activity, context, view, bean.name, bean.link, bean.id, false, needCollect = false)
                    }
                }
            }

            val chipsLayoutManager = ChipsLayoutManager.newBuilder(context)
                    //set vertical gravity for all items in a row. Default = Gravity.CENTER_VERTICAL
                    .setChildGravity(Gravity.LEFT)
                    //whether RecyclerView can scroll. TRUE by default
                    .setScrollingEnabled(true)
                    //a layoutOrientation of layout manager, could be VERTICAL OR HORIZONTAL.
                    // HORIZONTAL by default
                    .setOrientation(ChipsLayoutManager.HORIZONTAL)
                    // row strategy for views in completed row, could be STRATEGY_DEFAULT,
                    // STRATEGY_FILL_VIEW,
                    //STRATEGY_FILL_SPACE or STRATEGY_CENTER
                    .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                    // whether strategy is applied to last row. FALSE by default
                    .withLastRow(true)
                    .build()

            rvCommon?.apply {
                layoutManager = chipsLayoutManager
                adapter = commonWebAdapter
            }
        } else {
            commonWebAdapter!!.replaceData(t)
        }
    }

    private fun initCommonWebPresenter(): CommonWebPresenter {

        val view = object : CommonWebView() {

            override fun success(t: List<CommonWebBean>, refreshData: Boolean) {
                setAdapter(t)
            }
        }

        return CommonWebPresenter(this, view)
    }

    override fun refreshView() {

        if (!resume) {
            return
        }

        rvCommon?.scrollToPosition(0)
    }
}
