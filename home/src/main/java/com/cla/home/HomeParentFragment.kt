package com.cla.home

import com.cla.home.common.CommonWebFragment
import com.cla.home.main.HomeFragment
import com.konsung.basic.bean.TwoBean
import com.konsung.basic.ui.BasicFragment
import com.konsung.basic.presenter.BasicPresenter
import com.konsung.basic.ui.VpBasicFragment

/**
 * 首页
 */
class HomeParentFragment : VpBasicFragment() {

    override fun initPresenters(): List<BasicPresenter>? = null

    override fun initView() {
        val homeFragment = HomeFragment()
        homeFragment.fragmentRefresh = fragmentRefresh
        homeFragment.homeIndex = index
        val commonFragment = CommonWebFragment()

        val f1 = TwoBean<String, BasicFragment>("主页", homeFragment)
        val f2 = TwoBean<String, BasicFragment>("常用网站", commonFragment)
        initViewPager(listOf(f1, f2))
    }

    override fun initEvent() {

    }

    override fun initData() {
        showContentView()
    }

    override fun resetData() {

    }
}