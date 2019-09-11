package com.konsung.cla.demo2.main.fragment

import com.konsung.basic.bean.TwoBean
import com.konsung.basic.ui.BasicFragment
import com.konsung.basic.ui.VpBasicFragment
import com.konsung.cla.demo2.main.fragment.common.CommonWebFragment
import com.konsung.cla.demo2.main.fragment.home.HomeFragment

class HomeParentFragment : VpBasicFragment() {

    override fun initView() {
        val homeFragment = HomeFragment()
        homeFragment.fragmentRefresh = fragmentRefresh
        val commonFragment = CommonWebFragment()

        val f1 = TwoBean<String, BasicFragment>("主页", homeFragment)
        val f2 = TwoBean<String, BasicFragment>("常用网站", commonFragment)
        initViewPager(listOf(f1, f2))
    }

    override fun firstShow() {
        showContentView()
    }
}