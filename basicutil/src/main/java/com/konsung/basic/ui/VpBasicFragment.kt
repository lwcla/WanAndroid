package com.konsung.basic.ui

import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.konsung.basic.bean.TwoBean
import com.konsung.basic.util.R
import com.konsung.basic.util.StringUtils
import com.konsung.basic.util.toast
import com.konsung.basic.view.ColorTransitionPagerTitleView
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator


abstract class VpBasicFragment : BasicFragment() {

    companion object {
        val TAG: String = VpBasicFragment::class.java.simpleName
    }

    init {
        needDelayInitView = false
    }

    var titleList: List<TwoBean<String, BasicFragment>>? = null

    override fun getLayoutId(): Int = R.layout.fragment_vp_basic

    protected fun initViewPager(fragmentList: List<TwoBean<String, BasicFragment>>) {

        if (fragmentList.isEmpty() || context == null) {
            toast(TAG, R.string.data_error)
            return
        }

        if (fragmentList.isEmpty()) {
            showErrorView()
            return
        }

        this.titleList = fragmentList

        for (i in fragmentList.indices) {
            val threeBean = fragmentList[i]
            threeBean.b.index = i
        }

        val magicIndicator = showView?.findViewById<MagicIndicator>(R.id.magicIndicator) ?: return
        val viewPager = showView?.findViewById<ViewPager>(R.id.viewPager) ?: return

        viewPager.adapter = VpFragmentPagerAdapter(childFragmentManager, titleList!!)

        val commonNavigator = CommonNavigator(context)
        commonNavigator.adapter = object : CommonNavigatorAdapter() {

            override fun getTitleView(context: Context?, index: Int): IPagerTitleView {
                val clipPagerTitleView = ColorTransitionPagerTitleView(context)
                clipPagerTitleView.text = StringUtils.instance.formHtml(titleList?.get(index)?.a)
                clipPagerTitleView.normalColor = ContextCompat.getColor(context!!, R.color.normal_color2)
                clipPagerTitleView.selectedColor = Color.WHITE

                clipPagerTitleView.setOnClickListener {

                    if (viewPager.currentItem != index) {
                        //点击之后切换到对应的fragment
                        viewPager.currentItem = index
                        return@setOnClickListener
                    }
                }

                return clipPagerTitleView
            }

            override fun getCount(): Int {
                return titleList?.size ?: 0
            }

            override fun getIndicator(context: Context): IPagerIndicator? {
                val indicator = LinePagerIndicator(context)
                indicator.setColors(Color.WHITE)
                return indicator
            }
        }

        magicIndicator.navigator = commonNavigator
        ViewPagerHelper.bind(magicIndicator, viewPager)

        if (fragmentList.size == 1) {
            magicIndicator.visibility = View.GONE
        } else {
            magicIndicator.visibility = View.VISIBLE
        }
    }

    override fun refreshView() {

        if (!resume) {
            return
        }

        titleList?.let {
            for (two in it) {
                two.b.refreshView()
            }
        }
    }
}

class VpFragmentPagerAdapter(fm: FragmentManager, private val fragmentList: List<TwoBean<String, BasicFragment>>) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment = fragmentList[position].b

    override fun getCount(): Int = fragmentList.size

    override fun getPageTitle(position: Int): CharSequence? = fragmentList[position].a
}