package com.konsung.cla.demo2.aty

import android.content.Context
import android.content.IntentFilter
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.cla.home.HomeParentFragment
import com.cla.navigation.NavigationFragment
import com.cla.project.tree.parent.ProjectParentFragment
import com.cla.system.tree.list.SystemTreeListFragment
import com.cla.wx.article.parent.WxParentFragment
import com.konsung.basic.bean.ThreeBean
import com.konsung.basic.config.BaseConfig
import com.konsung.basic.presenter.LogoutPresenter
import com.konsung.basic.presenter.LogoutView
import com.konsung.basic.receiver.CollectReceiver
import com.konsung.basic.ui.BasicAty
import com.konsung.basic.ui.BasicFragment
import com.konsung.basic.presenter.BasicPresenter
import com.konsung.basic.presenter.Presenter
import com.konsung.basic.ui.FragmentRefresh
import com.konsung.basic.util.*
import com.konsung.cla.demo2.App
import com.konsung.cla.demo2.R
import com.konsung.cla.demo2.adapter.MyFragmentPagerAdapter
import com.konsung.cla.demo2.dialog.LinkCollectDialog
import com.konsung.cla.demo2.view.ViewPagerTitle
import kotlinx.android.synthetic.main.activity_main.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import kotlin.system.exitProcess


open class MainActivity : BasicAty(), View.OnClickListener {
    companion object {

        val TAG: String = MainActivity::class.java.simpleName
    }
    private var tvHeadName: TextView? = null

    private var linkCollectDialog: LinkCollectDialog? = null
    private val collectReceiver = CollectReceiver()

    private val logoutPresenter by lazy { initLogoutPresenter() }
    private var lastBackTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intentFilter = IntentFilter(BaseConfig.COLLECT_RESULT_ACTION)
        LocalBroadcastManager.getInstance(this).registerReceiver(collectReceiver, intentFilter)
    }

    override fun onResume() {
        super.onResume()
        resetUserName()
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(collectReceiver)
    }

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initPresenter(): List<BasicPresenter>? = listOf(logoutPresenter)

    override fun initPresenterList(): List<Presenter>? = null

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

            when (item.itemId) {
                //搜索
                R.id.menu_search -> App.productUtils.startSearchAty(this)
                //退出登录
                R.id.logout -> logoutPresenter.logout()

                R.id.to_do -> App.productUtils.startToDoAty(this)

                //收藏列表
                R.id.menu_collect, R.id.add_link, R.id.site_collect -> {

                    if (!AppUtils.instance.hasLogined(context)) {
                        //还没有登录，打开登录界面
                        toast(TAG, R.string.please_login_first)
                        AppUtils.startLoginAty(context)
                        return@setNavigationItemSelectedListener true
                    }

                    when (item.itemId) {
                        //收藏列表
                        R.id.menu_collect -> App.productUtils.startCollectAty(this)
                        //添加站外文章收藏
                        R.id.add_link -> showLinkCollectDialog()
                        //网站收藏
                        R.id.site_collect -> App.productUtils.startSiteCollectAty(this)
                    }
                }
            }

//            val title = item.title.toString()
//            ToastUtils.instance.toast(context, TAG, title)
            //关闭的动画时间太短，影响体验，先不关闭了
//            drawerLayout.closeDrawer(nvLeft)
            true
        }
    }

    override fun initData() {
        Debug.info(TAG, "MainActivity initData")
    }

    private fun showLinkCollectDialog() {

        val tag = LinkCollectDialog.TAG

        linkCollectDialog = supportFragmentManager.findFragmentByTag(tag) as LinkCollectDialog?

        if (linkCollectDialog != null) {
            supportFragmentManager.beginTransaction().remove(linkCollectDialog!!).commitAllowingStateLoss()
        }

        linkCollectDialog = null
        linkCollectDialog = LinkCollectDialog()

        if (!linkCollectDialog!!.isAdded) {
            linkCollectDialog!!.show(supportFragmentManager, tag)
        }
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
        val rlHead = headView.findViewById<ConstraintLayout>(R.id.rlHead)
        rlHead.setOnClickListener(this)

        tvHeadName = headView.findViewById<TextView>(R.id.tvInfo)
    }

    private fun initLogoutPresenter(): LogoutPresenter {

        val view = object : LogoutView() {

            override fun success(refreshData: Boolean) {
                resetUserName()
            }
        }

        return LogoutPresenter(this, view)
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

        val fragmentList = listOf(officialAccount, system, home, navigation, project)
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
                    fragmentList[index].c.scrollToTop()
                }
                titleViewList.add(viewPagerTitle)
                return viewPagerTitle
            }
        }

        commonNavigator.adapter = commonAdapter
        magicIndicator.navigator = commonNavigator

        ViewPagerHelper.bind(magicIndicator, viewPager)
        viewPager.currentItem = homePage.index
//        viewPager.offscreenPageLimit = 3
    }

    private fun resetUserName() {
        val name = SpUtils.getString(context, BaseConfig.USER_NAME, "")

        if (name.isNullOrEmpty()) {
            tvHeadName?.text = getString(R.string.not_log_in)
            tvTitle.text = getString(R.string.not_log_in)
            //隐藏退出登录按钮
            nvLeft.menu.findItem(R.id.logout).isVisible = false
        } else {
            tvHeadName?.text = name
            tvTitle.text = name
            //显示退出登录按钮
            nvLeft.menu.findItem(R.id.logout).isVisible = true
        }
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.rivHead -> drawerLayout.openDrawer(nvLeft)

            R.id.rlHead -> App.productUtils.startLoginAty(this)

            R.id.tvIconSearch -> App.productUtils.startSearchAty(this)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {

        if (event.action == KeyEvent.ACTION_DOWN) {

            if (keyCode == KeyEvent.KEYCODE_BACK) {

                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    return true
                }

                if (lastBackTime == 0L) {
                    lastBackTime = System.currentTimeMillis()
                    toast(TAG, R.string.press_again_to_exit_the_application)
                    return true
                }

                val time = System.currentTimeMillis()
                return if (time - lastBackTime < 1000) {
                    finish()
                    exitProcess(0)
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
