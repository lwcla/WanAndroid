package com.konsung.cla.demo2.main.fragment

import com.konsung.basic.ui.BasicFragment
import com.konsung.basic.ui.BasicPresenter
import com.konsung.cla.demo2.R

class ProjectFragment : BasicFragment() {

    override fun getLayoutId(): Int = R.layout.fragment_project

    override fun getMultiplyId(): Int = R.id.multiplyStatusView

    override fun initView() {

    }

    override fun refreshData() {
    }

    override fun initPresenters(): List<BasicPresenter>? = null

}