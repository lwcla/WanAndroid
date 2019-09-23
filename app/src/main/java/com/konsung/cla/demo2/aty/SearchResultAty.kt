package com.konsung.cla.demo2.aty

import android.os.Bundle
import android.transition.Fade
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.classic.common.MultipleStatusView
import com.konsung.basic.bean.HomeData
import com.konsung.basic.config.BaseConfig
import com.konsung.basic.ui.BasicAty
import com.konsung.basic.ui.BasicPresenter
import com.konsung.cla.demo2.R
import com.konsung.cla.demo2.presenter.SearchResultPresenter
import com.konsung.cla.demo2.presenter.SearchResultView

class SearchResultAty : BasicAty() {

    companion object {
        const val INIT_DELAY = 500L
    }

    private var multiplyStatusView: MultipleStatusView? = null
    private var showView: View? = null

    private val searchResultPresenter by lazy { initSearchResultPresenter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.enterTransition = Fade().setDuration(INIT_DELAY)
        window.exitTransition = Fade().setDuration(INIT_DELAY)
    }

    override fun getLayoutId(): Int = R.layout.view_multiplee_status_container

    override fun initPresenter(): List<BasicPresenter>? = listOf(searchResultPresenter)

    override fun initView() {
        multiplyStatusView = findViewById(R.id.multiplyStatusView)
        multiplyStatusView?.showLoading()
    }

    override fun initEvent() {

    }

    override fun initData() {

    }

    private fun getKey(): String = intent.getStringExtra(BaseConfig.SEARCH_KEY)

    private fun initSearchResultPresenter(): SearchResultPresenter {

        val view = object : SearchResultView() {

            override fun success(t: List<HomeData.DatasBean>, refreshData: Boolean) {

            }
        }

        return SearchResultPresenter(this, view, getKey())
    }

    override fun showLoadView() {
        multiplyStatusView?.showLoading()
    }

    override fun showContentView() {

        var showed = true

        if (showView == null) {
            synchronized(this) {
                if (showView == null) {
                    showed = false
                    showView = LayoutInflater.from(this).inflate(R.layout.view_search_result, multiplyStatusView, false)
                }
            }
        }

        if (!showed) {
            val layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
            multiplyStatusView?.showContent(R.layout.view_search_result, layoutParams)
        } else {
            multiplyStatusView?.showContent()
        }

    }

    override fun showErrorView() {
        multiplyStatusView?.showError()
    }

    override fun showNoNetworkView() {
        multiplyStatusView?.showNoNetwork()
    }
}
