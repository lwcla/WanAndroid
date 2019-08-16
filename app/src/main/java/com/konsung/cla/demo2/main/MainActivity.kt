package com.konsung.cla.demo2.main

import android.content.Context
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.konsung.basic.bean.ThreeBean
import com.konsung.basic.net.NetChangeReceiver
import com.konsung.basic.ui.BasicAty
import com.konsung.basic.ui.BasicPresenter
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

        nvLeft.setNavigationItemSelectedListener { item ->
            val title = item.title.toString()
            ToastUtils.instance.toast(context, TAG, title)
//            drawerLayout.closeDrawer(nvLeft)
            true
        }
    }

    override fun initData() {
        Debug.info(TAG, "MainActivity initData")
//        RequestUtils.instance.getWeChatOfficial()
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

        val fragment1 = ThreeBean<String, Int, Fragment>(getString(R.string.home), R.string.icon_home, HomeFragment())
        val fragment2 = ThreeBean<String, Int, Fragment>(getString(R.string.project), R.string.icon_project, ProjectFragment())
        val fragment3 = ThreeBean<String, Int, Fragment>(getString(R.string.system), R.string.icon_system, SystemFragment())
        val fragment4 = ThreeBean<String, Int, Fragment>(getString(R.string.navigation), R.string.icon_navigation, NavigationFragment())
        val fragmentList = listOf(fragment1, fragment2, fragment3, fragment4)

        viewPager.adapter = MyFragmentPagerAdapter(supportFragmentManager, fragmentList)

        val commonNavigator = CommonNavigator(this)
        commonNavigator.isAdjustMode = true
        val commonAdapter = object : CommonNavigatorAdapter() {

            override fun getCount(): Int = fragmentList.size

            override fun getIndicator(context: Context?): IPagerIndicator? = null

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val threeBean = fragmentList[index]
                val viewPagerTitle = ViewPagerTitle(context, threeBean.b, threeBean.a)
                viewPagerTitle.setOnClickListener { viewPager.currentItem = index }
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

        }
    }
}
