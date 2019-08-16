package com.konsung.cla.demo2.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.konsung.basic.bean.ThreeBean

class MyFragmentPagerAdapter(fm: FragmentManager, private val fragmentList: List<ThreeBean<String, Int, Fragment>>) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment = fragmentList[position].c

    override fun getCount(): Int = fragmentList.size

    override fun getPageTitle(position: Int): CharSequence? = fragmentList[position].a
}