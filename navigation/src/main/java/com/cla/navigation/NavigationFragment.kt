package com.cla.navigation

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cla.navigation.adapter.DWStickItemDecoration
import com.cla.navigation.adapter.NavigationAdapter
import com.cla.navigation.utils.NavigationUtils
import com.cla.navigation.utils.PinyinComparator
import com.cla.navigation.view.SideBar
import com.konsung.basic.bean.HomeData
import com.konsung.basic.bean.navigation.NavigationBean
import com.konsung.basic.ui.BasicFragment
import com.konsung.basic.presenter.BasicPresenter
import com.konsung.basic.util.AppUtils
import com.konsung.basic.util.ConvertUtils
import java.util.*


class NavigationFragment : BasicFragment() {

    private var sideBar: SideBar? = null
    private var text: TextView? = null

    private val loadNavigation by lazy { initLoadNavigation() }

    private var manager: LinearLayoutManager? = null
    private var navigationAdapter: NavigationAdapter? = null
    private val hideSideBarRunnable = Runnable {
        sideBar?.visibility = View.GONE
    }

    override fun getLayoutId(): Int = R.layout.fragment_navigation

    override fun initPresenters(): List<BasicPresenter>? = listOf(loadNavigation)

    override fun initView() {
        refreshRv = showView?.findViewById(R.id.refreshRv)
        refreshRv?.setEnableLoadMore(false)
        sideBar = showView?.findViewById(R.id.side)
        text = showView?.findViewById(R.id.text)

        sideBar?.apply {
            setTextView(text)
            setTextColor(ContextCompat.getColor(context, R.color.white))
            setTextSelectColor(ContextCompat.getColor(context, R.color.black))
            setTextSize(ConvertUtils.sp2px(context, 13.toFloat()))
            setBackgroundResource(R.drawable.sidebar_background)
        }
    }

    override fun initEvent() {
        refreshRv?.setRefreshListener(fragmentRefresh, index) {
            refreshRv?.autoRefresh()
            resetData()
        }

        refreshRv?.rv?.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                //newState ==0 时表示滚动停止
                if (newState == 0) {
                    myHandler.postDelayed(hideSideBarRunnable, 2000)
                } else {
                    myHandler.removeCallbacks(hideSideBarRunnable)
                    sideBar?.visibility = View.VISIBLE
                }
            }
        })

        //设置右侧SideBar触摸监听
        sideBar?.setOnTouchingLetterChangedListener { s ->
            //该字母首次出现的位置
            val position = navigationAdapter?.getPositionForSection(s[0].toInt())

            if (position == null || position == -1) {
                return@setOnTouchingLetterChangedListener
            }

//            refreshRv?.rv?.smoothScrollToPosition(position)
            manager?.scrollToPositionWithOffset(position, 0)
        }

    }

    override fun initData() {
        resetData()
    }

    private fun showData(t: List<NavigationBean>) {

        val sourceDateList = NavigationUtils.filledData(t)
        val pinyinComparator = PinyinComparator()
        // 根据a-z进行排序源数据
        Collections.sort(sourceDateList, pinyinComparator)

        val decoration = object : DWStickItemDecoration(context) {
            override fun isShowItemLabel(position: Int): Boolean {
                return sourceDateList[position].isShowLabel
            }

            override fun getItemLabelStr(position: Int): String {
                return sourceDateList[position].letters
            }
        }

        val nameSet = mutableSetOf<String>()
        for (data in sourceDateList) {
            nameSet.add(data.letters)
        }
        sideBar?.refreshLabels(nameSet.toList())

        //设置标签颜色
        decoration.setLabelColor(ContextCompat.getColor(context!!, R.color.normal_color1))
        //设置标签高度
        decoration.setLabelHeight(ConvertUtils.dp2px(context!!, 25.toFloat()))
        //设置标签字体颜色
        decoration.setTextColor(ContextCompat.getColor(context!!, R.color.white))
        //设置标签字体大小
        decoration.setTextSize(ConvertUtils.sp2px(context!!, 15.toFloat()))

        if (navigationAdapter == null) {
            manager = LinearLayoutManager(context)
            manager?.orientation = RecyclerView.VERTICAL
            navigationAdapter = NavigationAdapter(sourceDateList)
            navigationAdapter?.clickListener = View.OnClickListener {

                val data = it.getTag(R.id.recycler_view_adapter_item_click) as? HomeData.DatasBean
                        ?: return@OnClickListener

                AppUtils.startWebAty(activity, context, it, data.title, data.link, data.id, data.collect, needCollect = false)
            }

            refreshRv?.rv?.apply {
                addItemDecoration(decoration)
                layoutManager = manager
                adapter = navigationAdapter
            }
        } else {
            navigationAdapter?.update(sourceDateList)
        }
    }

    private fun initLoadNavigation(): LoadNavigation {

        val view = object : LoadNavigationView() {

            override fun success(t: List<NavigationBean>, refreshData: Boolean) {
                showData(t)
            }
        }

        return LoadNavigation(this, view)
    }

    override fun resetData() {
        loadNavigation.load()
    }

    override fun refreshView() {
        refreshRv?.refreshDataAfterScrollTop()
    }
}