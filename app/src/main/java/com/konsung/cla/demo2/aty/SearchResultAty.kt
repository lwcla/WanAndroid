package com.konsung.cla.demo2.aty

import android.os.Bundle
import android.transition.Fade
import com.konsung.basic.ui.BasicAty
import com.konsung.basic.ui.BasicPresenter
import com.konsung.cla.demo2.R

class SearchResultAty : BasicAty() {

    companion object {
        const val INIT_DELAY = 500L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.enterTransition = Fade().setDuration(INIT_DELAY)
        window.exitTransition = Fade().setDuration(INIT_DELAY)
    }

    override fun getLayoutId(): Int = R.layout.activity_search_result

    override fun initPresenter(): List<BasicPresenter>? = null

    override fun initView() {

    }

    override fun initEvent() {

    }

    override fun initData() {

    }
}
