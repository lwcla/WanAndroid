package com.konsung.cla.demo2.main.fragment

import com.konsung.basic.ui.BasicFragment
import com.konsung.basic.ui.BasicPresenter
import com.konsung.cla.demo2.R

/**
 * 体系
 */
class SystemFragment : BasicFragment() {

    override fun getLayoutId(): Int = R.layout.fragment_system

    override fun getMultiplyId(): Int = R.id.multiplyStatusView

    override fun initView() {

    }

    override fun resetData() {
    }

    override fun initPresenters(): List<BasicPresenter>? = null

}