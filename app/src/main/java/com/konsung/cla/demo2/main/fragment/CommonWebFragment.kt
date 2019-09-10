package com.konsung.cla.demo2.main.fragment

import com.konsung.basic.ui.BasicFragment
import com.konsung.basic.ui.BasicPresenter
import com.konsung.cla.demo2.R

/**
 * 常用网站
 */
class CommonWebFragment : BasicFragment() {

    override fun getLayoutId(): Int = R.layout.fragment_common_web

    override fun initPresenters(): List<BasicPresenter>? = null

    override fun initView() {
    }

    override fun resetData() {
    }

    override fun firstShow() {
        showContentView()
    }

}