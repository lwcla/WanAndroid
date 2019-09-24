package com.konsung.basic.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.konsung.basic.util.R
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.ClassicsFooter

class RefreshRecyclerView : SmartRefreshLayout {

    companion object {
        val TAG: String = RefreshRecyclerView::class.java.simpleName
    }

    var rv: RecyclerView

    var needRefresh = false

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.view_refresh_recycler, this, true)
        rv = view.findViewById(R.id.rv)

        setRefreshFooter(ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Scale))
        setRefreshHeader(MaterialHeader(context).setShowBezierWave(false))
        setPrimaryColorsId(R.color.colorPrimary, android.R.color.white, R.color.colorAccent, R.color.btn_search_color)
    }

    /**
     * 点击刷新按钮之后，滚动到顶部
     */
    fun refreshDataAfterScrollTop() {
        needRefresh = true
        rv.apply {
            if (layoutManager is LinearLayoutManager) {
                val position = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                if (position > 10) {
                    //先滚动到10的位置，然后再以动画的方式滚动到顶部
                    scrollToPosition(10)
                }
            }

            smoothScrollToPosition(0)
        }
    }

    /**
     * 初始化recyclerView
     * @param myAdapter adapter，继承于BaseQuickAdapter
     * @param fragmentRefresh 根据recyclerView的滚动位置，通知当前是否为刷新状态
     * @param index 当前fragment的位置
     * @param refreshData 在点击刷新按钮滚动到顶部之后，可能需要刷新页面的操作
     */
    inline fun <T> initRecyclerView(myAdapter: BaseQuickAdapter<T, BaseViewHolder>?, fragmentRefresh: FragmentRefresh?, index: Int, hasHead: Boolean = false, crossinline refreshData: () -> Unit = {}) {

        rv.itemAnimator?.changeDuration = 0

        val space = context.resources.getDimension(R.dimen.dp_10)
        rv.addItemDecoration(SpaceDecoration(space.toInt(), hasHead))
        rv.adapter = myAdapter
        val manager = LinearLayoutManager(context)
        rv.layoutManager = manager

        setRefreshListener(fragmentRefresh, index, refreshData)
    }

    /**
     * 滚动到顶部之后刷新数据
     * @param fragmentRefresh 根据recyclerView的滚动位置，通知当前是否为刷新状态
     * @param refreshData 在点击刷新按钮滚动到顶部之后，可能需要刷新页面的操作
     */
    inline fun setRefreshListener(fragmentRefresh: FragmentRefresh?, index: Int, crossinline refreshData: () -> Unit = {}) {

        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                        Debug.info(TAG, "HomeFragment onScrollStateChanged newState=$newState")

                //newState ==0 时表示滚动停止
                if (newState != 0) {
                    return
                }

                //recyclerView.canScrollVertically(-1)为false时表示滚动到顶部
                if (recyclerView.canScrollVertically(-1)) {
                    return
                }

                //已经滚动到顶部，修改当前状态
                fragmentRefresh?.refresh(false, index)

                if (needRefresh) {
                    needRefresh = false
                    refreshData.invoke()
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                        Debug.info(TAG, "HomeFragment onScrolled dy=$dy")
                if (dy > 0) {
                    //向下滚动
                    fragmentRefresh?.refresh(true, index)
                }
            }
        })
    }
}
