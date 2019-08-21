package com.konsung.cla.demo2.main

import android.content.Context
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.konsung.basic.bean.ThreeBean
import com.konsung.basic.net.NetChangeReceiver
import com.konsung.basic.ui.BasicAty
import com.konsung.basic.ui.BasicFragment
import com.konsung.basic.ui.BasicPresenter
import com.konsung.basic.ui.FragmentRefresh
import com.konsung.basic.util.Debug
import com.konsung.basic.util.StringUtils
import com.konsung.basic.util.ToastUtils
import com.konsung.cla.demo2.R
import com.konsung.cla.demo2.adapter.MyFragmentPagerAdapter
import com.konsung.cla.demo2.main.fragment.NavigationFragment
import com.konsung.cla.demo2.main.fragment.ProjectFragment
import com.konsung.cla.demo2.main.fragment.SystemFragment
import com.konsung.cla.demo2.main.fragment.home.HomeFragment
import com.konsung.cla.demo2.main.view.ViewPagerTitle
import kotlinx.android.synthetic.main.activity_main.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView


open class MainActivity : BasicAty(), View.OnClickListener {

    private val netChangeReceiver = NetChangeReceiver()


    companion object {
        val TAG: String = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val filter = IntentFilter()
        filter.addAction("android.NET.conn.CONNECTIVITY_CHANGE")
        filter.addAction("android.Net.wifi.WIFI_STATE_CHANGED")
        filter.addAction("android.net.wifi.STATE_CHANGE")

        registerReceiver(netChangeReceiver, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(netChangeReceiver)
    }

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initPresenter(): List<BasicPresenter>? = null

    override fun initView() {
        Debug.info(TAG, "MainActivity initView")
        tvTitle.text = getString(R.string.not_log_in)
        StringUtils.instance.loadTextIcon(context, tvIconSearch)
        initNavigationView()
        initViewPager()
    }

    override fun initEvent() {

        Debug.info(TAG, "MainActivity initEvent")

        rivHead.setOnClickListener(this)
        tvIconSearch.setOnClickListener(this)

        nvLeft.setNavigationItemSelectedListener { item ->
            val title = item.title.toString()
            ToastUtils.instance.toast(context, TAG, title)
//            drawerLayout.closeDrawer(nvLeft)
            true
        }
    }

    override fun initData() {
        Debug.info(TAG, "MainActivity initData")
    }

    /**
     * 初始化左边抽屉界面
     */
    private fun initNavigationView() {
        val headView = nvLeft.inflateHeaderView(R.layout.view_aty_main_left_navigation)
        val tvInfo = headView.findViewById<TextView>(R.id.tvInfo)
        tvInfo.text = getString(R.string.not_log_in)
    }

    /**
     * 初始化viewPager
     */
    private fun initViewPager() {

        lateinit var commonAdapter: CommonNavigatorAdapter
        val titleViewList = mutableListOf<ViewPagerTitle>()
        //当recyclerView往下拉的时候，把图标变为刷新的样子，点击之后滚动到顶部并刷新数据
        val fragmentRefresh = object : FragmentRefresh {
            override fun refresh(isRefresh: Boolean, index: Int) {

                if (index >= titleViewList.size) {
                    return
                }

                val titleView = titleViewList[index]
                titleView.apply {
                    if (isRefresh) {
                        setIcon(R.string.icon_refresh)
                        tvName.visibility = View.GONE
                    } else {
                        initIcon()
                        tvName.visibility = View.VISIBLE
                    }
                }
            }
        }

        val homeFragment = HomeFragment()
        homeFragment.fragmentRefresh = fragmentRefresh
        val fragment1 = ThreeBean<String, Int, BasicFragment>(getString(R.string.home), R.string.icon_home, homeFragment)
        val fragment2 = ThreeBean<String, Int, BasicFragment>(getString(R.string.project), R.string.icon_project, ProjectFragment())
        val fragment3 = ThreeBean<String, Int, BasicFragment>(getString(R.string.system), R.string.icon_system, SystemFragment())
        val fragment4 = ThreeBean<String, Int, BasicFragment>(getString(R.string.navigation), R.string.icon_navigation, NavigationFragment())
        val fragmentList = listOf(fragment1, fragment2, fragment3, fragment4)

        for (i in 0 until fragmentList.size) {
            val threeBean = fragmentList[i]
            threeBean.c.index = i
        }

        viewPager.adapter = MyFragmentPagerAdapter(supportFragmentManager, fragmentList)

        val commonNavigator = CommonNavigator(this)
        commonNavigator.isAdjustMode = true
        commonAdapter = object : CommonNavigatorAdapter() {

            override fun getCount(): Int = fragmentList.size

            override fun getIndicator(context: Context?): IPagerIndicator? = null

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val threeBean = fragmentList[index]
                val viewPagerTitle = ViewPagerTitle(context, threeBean.b, threeBean.a)
                viewPagerTitle.setOnClickListener {
                    if (viewPager.currentItem != index) {
                        //点击之后切换到对应的fragment
                        viewPager.currentItem = index
                        return@setOnClickListener
                    }

                    //点击的是当前fragment的下标，那就刷新本页
                    fragmentList[index].c.refreshView()
                }
                titleViewList.add(viewPagerTitle)
                return viewPagerTitle
            }
        }

        commonNavigator.adapter = commonAdapter
        magicIndicator.navigator = commonNavigator

        ViewPagerHelper.bind(magicIndicator, viewPager)
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.rivHead -> {
                drawerLayout.openDrawer(nvLeft)
            }

            R.id.tvIconSearch -> {
                Snackbar.make(coordinator, "点击搜索", Snackbar.LENGTH_INDEFINITE)
                        .setAction("搜索") {
                            ToastUtils.instance.toast(context, TAG, "点击有效")
                        }
                        .show()
            }

        }
    }
}
