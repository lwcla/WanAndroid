package com.konsung.cla.demo2.main

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.konsung.basic.ui.RefreshRecyclerView
import com.konsung.basic.util.Debug


/**
 * 参考 https://www.jianshu.com/p/b987fad8fcb4
 */
class HeaderBehavior(context: Context, attrs: AttributeSet) : CoordinatorLayout.Behavior<Toolbar>(context, attrs) {

    companion object {
        val TAG = HeaderBehavior::class.java.simpleName
    }

    // 界面整体向上滑动，达到列表可滑动的临界点
    private var upReach: Boolean = false
    // 列表向上滑动后，再向下滑动，达到界面整体可滑动的临界点
    private var downReach: Boolean = false
    // 列表上一个全部可见的item位置
    private var lastPosition = -1


    override fun onInterceptTouchEvent(parent: CoordinatorLayout, child: Toolbar, ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                downReach = false
                upReach = false
            }
        }
        return super.onInterceptTouchEvent(parent, child, ev)
    }

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: Toolbar, directTargetChild: View, target: View, axes: Int, type: Int): Boolean {
        return axes and ViewCompat.SCROLL_AXIS_VERTICAL != 0
//        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedPreScroll(coordinatorLayout: CoordinatorLayout, child: Toolbar, target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
//        Debug.info(TAG, "HeaderBehavior onNestedPreScroll dy=$dy target=$target")
        if (target is RecyclerView) {
            // 列表第一个全部可见Item的位置
            val pos = (target.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()

            Debug.info(TAG, "HeaderBehavior onNestedPreScroll pos=$pos lastPosition=$lastPosition")

            if (pos == 0 && pos < lastPosition) {
                downReach = true
            }

            // 整体可以滑动，否则RecyclerView消费滑动事件
            if (canScroll(child, dy.toFloat()) && pos == 0) {
                //dy表示手指移动的y轴距离
                var finalY = child.y - dy
                //判断textView这次滑动的局流是否超过其高度
                if (finalY < -child.height) {
                    finalY = (-child.height).toFloat()
                    upReach = true
                } else if (finalY > 0) {
                    finalY = 0f
                }

                Debug.info(TAG, "HeaderBehavior onNestedPreScroll finalY=$finalY")
                child.y = finalY
                // 让CoordinatorLayout消费滑动事件
                consumed[1] = dy
            }
            lastPosition = pos
        }
    }

    private fun canScroll(child: Toolbar, scrollY: Float): Boolean {
        //scrollY大于0表示列表向上滑动
        //child.translationY.toInt() == -child.height表示TextView已经滑动到屏幕之外了
        //upReach表示当前是否已经过了临界点
        println("lwl scrollY=$scrollY child.y=${child.y} child.height=${child.height} upReach?$upReach")
        if (scrollY > 0 && child.y.toInt() == -child.height && !upReach) {
            return false
        }

        return !downReach
    }
}