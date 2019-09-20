package com.konsung.cla.demo2.aty

import android.content.Context
import android.content.IntentFilter
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.cla.home.HomeParentFragment
import com.cla.navigation.NavigationFragment
import com.cla.project.tree.parent.ProjectParentFragment
import com.cla.system.tree.list.SystemTreeListFragment
import com.cla.wx.article.parent.WxParentFragment
import com.google.android.material.snackbar.Snackbar
import com.konsung.basic.bean.ThreeBean
import com.konsung.basic.config.BaseConfig
import com.konsung.basic.net.NetChangeReceiver
import com.konsung.basic.ui.BasicAty
import com.konsung.basic.ui.BasicFragment
import com.konsung.basic.ui.BasicPresenter
import com.konsung.basic.ui.FragmentRefresh
import com.konsung.basic.util.*
import com.konsung.cla.demo2.App
import com.konsung.cla.demo2.R
import com.konsung.cla.demo2.adapter.MyFragmentPagerAdapter
import com.konsung.cla.demo2.view.ViewPagerTitle
import kotlinx.android.synthetic.main.activity_main.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView


open class MainActivity : BasicAty(), View.OnClickListener {

    companion object {
        val TAG: String = MainActivity::class.java.simpleName
    }

    private val netChangeReceiver = NetChangeReceiver()

    private var tvHeadName: TextView? = null
    private var lastBackTime = 0L

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initPresenter(): List<BasicPresenter>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val filter = IntentFilter()
        filter.addAction("android.NET.conn.CONNECTIVITY_CHANGE")
        filter.addAction("android.Net.wifi.WIFI_STATE_CHANGED")
        filter.addAction("android.net.wifi.STATE_CHANGE")

        registerReceiver(netChangeReceiver, filter)
    }

    override fun onResume() {
        super.onResume()
        initUserName()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(netChangeReceiver)
    }

    override fun initView() {
        Debug.info(TAG, "MainActivity initView")
        tvTitle.text = getString(R.string.not_log_in)
        StringUtils.instance.loadTextIcon(context, tvIconSearch)
        initToolBar()
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
            //关闭的动画时间太短，影响体验，先不关闭了
//            drawerLayout.closeDrawer(nvLeft)
            true
        }
    }

    override fun initData() {
        Debug.info(TAG, "MainActivity initData")
    }

    private fun initToolBar() {
        val drawerToggle = object : ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name) {
            override fun onDrawerOpened(drawerView: View) {//完全打开时触发
                super.onDrawerOpened(drawerView)
            }

            override fun onDrawerClosed(drawerView: View) {//完全关闭时触发
                super.onDrawerClosed(drawerView)
            }

            /**
             * 当抽屉被滑动的时候调用此方法
             * slideOffset表示 滑动的幅度（0-1）
             */
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)
            }

            /**
             * 当抽屉滑动状态改变的时候被调用
             * 状态值是STATE_IDLE（闲置--0）, STATE_DRAGGING（拖拽的--1）, STATE_SETTLING（固定--2）中之一。
             * 具体状态可以慢慢调试
             */
            override fun onDrawerStateChanged(newState: Int) {
                super.onDrawerStateChanged(newState)
            }
        }

        //通过下面这句实现toolbar和Drawer的联动：如果没有这行代码，箭头是不会随着侧滑菜单的开关而变换的（或者没有箭头），
        // 可以尝试一下，不影响正常侧滑
        drawerToggle.syncState()
        drawerLayout.addDrawerListener(drawerToggle)

        setSupportActionBar(toolbar)
        //toolbar设置的图标控制drawerlayout的侧滑
        toolbar.setNavigationOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }
    }

    /**
     * 初始化左边抽屉界面
     */
    private fun initNavigationView() {
        val headView = nvLeft.inflateHeaderView(R.layout.view_aty_main_left_navigation)
        val rlHead = headView.findViewById<RelativeLayout>(R.id.rlHead)
        rlHead.setOnClickListener(this)

        tvHeadName = headView.findViewById<TextView>(R.id.tvInfo)
    }

    private fun initUserName() {
        val userName = SpUtils.getString(context, BaseConfig.USER_NAME, "")
        if (userName.isNullOrEmpty()) {
            tvHeadName?.text = getString(R.string.not_log_in)
            tvTitle.text = getString(R.string.not_log_in)

        } else {
            tvHeadName?.text = userName
            tvTitle.text = userName
        }
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

        val homePage = HomeParentFragment()
        homePage.fragmentRefresh = fragmentRefresh

        val home = ThreeBean<String, Int, BasicFragment>(getString(R.string.home), R.string.icon_home, homePage)
        val project = ThreeBean<String, Int, BasicFragment>(getString(R.string.project), R.string.icon_project, ProjectParentFragment())
        val system = ThreeBean<String, Int, BasicFragment>(getString(R.string.system), R.string.icon_system, SystemTreeListFragment())
        val navigation = ThreeBean<String, Int, BasicFragment>(getString(R.string.navigation), R.string.icon_navigation, NavigationFragment())
        val officialAccount = ThreeBean<String, Int, BasicFragment>(getString(R.string.official_accounts), R.string.icon_official_accounts, WxParentFragment())

        val fragmentList = listOf(home, system, officialAccount, navigation, project)
//        val fragmentList = listOf(navigation, home, system, officialAccount, project)

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

            R.id.rivHead -> drawerLayout.openDrawer(nvLeft)

            R.id.rlHead -> App.productUtils.startLoginAty(this)

            R.id.tvIconSearch -> {
                Snackbar.make(coordinator, "点击搜索", Snackbar.LENGTH_INDEFINITE)
                        .setAction("搜索") {
                            ToastUtils.instance.toast(context, TAG, "点击有效")
                        }
                        .show()
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {

        if (event.action == KeyEvent.ACTION_DOWN) {

            if (keyCode == KeyEvent.KEYCODE_BACK) {

                if (lastBackTime == 0L) {
                    lastBackTime = System.currentTimeMillis()
                    toast(TAG, R.string.press_again_to_exit_the_application)
                    return true
                }

                val time = System.currentTimeMillis()
                return if (time - lastBackTime < 1000) {
                    finish()
                    true
                } else {
                    lastBackTime = time
                    toast(TAG, R.string.press_again_to_exit_the_application)
                    true
                }
            }
        }

        return super.onKeyDown(keyCode, event)
    }
}
