package com.cla.system.tree.detail

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.transition.Explode
import android.view.View
import androidx.core.content.ContextCompat
import com.cla.system.tree.R
import com.konsung.basic.bean.TwoBean
import com.konsung.basic.bean.tree.SystemTreeTitle
import com.konsung.basic.config.BaseConfig
import com.konsung.basic.ui.BasicAty
import com.konsung.basic.ui.BasicFragment
import com.konsung.basic.ui.BasicPresenter
import com.konsung.basic.ui.VpFragmentPagerAdapter
import com.konsung.basic.util.StringUtils
import com.konsung.basic.util.toast
import com.konsung.basic.view.ColorTransitionPagerTitleView
import kotlinx.android.synthetic.main.activity_system_tree_detail.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator

class SystemTreeDetailAty : BasicAty() {

    companion object {
        val TAG: String = SystemTreeDetailAty::class.java.simpleName
        const val INIT_DETAIL = 300L
    }

    init {
        initDelay = INIT_DETAIL
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.enterTransition = Explode().setDuration(INIT_DETAIL)
        window.exitTransition = Explode().setDuration(INIT_DETAIL)
    }

    override fun getLayoutId(): Int = R.layout.activity_system_tree_detail

    override fun initPresenter(): List<BasicPresenter>? = null

    override fun initView() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun initEvent() {
        toolbar.setNavigationOnClickListener {
            finishAfterTransition()
        }
    }

    override fun initData() {

        val systemTreeTitle = intent.getSerializableExtra(BaseConfig.SYSTEM_TREE_TITLE_LIST) as? SystemTreeTitle

        if (systemTreeTitle == null || systemTreeTitle.map.isEmpty()) {
            toast(TAG, R.string.fragment_list_is_null)
            finish()
            return
        }

        supportActionBar?.title = systemTreeTitle.activityTitle

        val fragmentList = mutableListOf<TwoBean<String, BasicFragment>>()
        for ((fragmentTitle, id) in systemTreeTitle.map) {

            val fragment = SystemTreeDetailFragment()
            fragment.cid = id
            fragmentList.add(TwoBean(fragmentTitle, fragment))
        }

        for (i in 0 until fragmentList.size) {
            val threeBean = fragmentList[i]
            threeBean.b.index = i
        }

        viewPager.adapter = VpFragmentPagerAdapter(supportFragmentManager, fragmentList)

        val commonNavigator = CommonNavigator(context)
        commonNavigator.adapter = object : CommonNavigatorAdapter() {

            override fun getTitleView(context: Context?, index: Int): IPagerTitleView {
                val clipPagerTitleView = ColorTransitionPagerTitleView(context)
                clipPagerTitleView.text = StringUtils.instance.formHtml(fragmentList[index].a)
                clipPagerTitleView.normalColor = ContextCompat.getColor(context!!, com.konsung.basic.util.R.color.normal_color2)
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
                return fragmentList.size
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

        var currentIndex = systemTreeTitle.currentIndex
        if (currentIndex < 0) {
            currentIndex = 0
        }

        viewPager.currentItem = currentIndex
    }

}



