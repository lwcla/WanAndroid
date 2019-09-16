package com.cla.system.tree.view

import am.widget.wraplayout.WrapLayout
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.cla.system.tree.R
import com.konsung.basic.bean.tree.SystemTreeListBean
import com.konsung.basic.util.ConvertUtils
import com.konsung.basic.util.Debug
import com.konsung.basic.util.Utils

class TreeListContentView(context: Context, attrs: AttributeSet) : WrapLayout(context, attrs) {

    companion object {
        val TAG: String = TreeListContentView::class.java.simpleName
    }

    init {
        val space = ConvertUtils.dp2px(context, 15.toFloat())
        horizontalSpacing = space
        verticalSpacing = space
        gravity = GRAVITY_CENTER
    }

    fun update(bean: SystemTreeListBean) {

        val dataSize = bean.children.size

        if (dataSize == 0) {
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                child.visibility = View.GONE
            }
            return
        }

        if (childCount >= dataSize) {
            for (i in dataSize until childCount) {
                val child = getChildAt(i)
                child.visibility = View.GONE
            }
        } else {
            for (i in childCount until dataSize) {
                val child = getChildView()
                child.visibility = View.VISIBLE
                addView(child)
            }
        }

        for (i in 0 until dataSize) {
            val child = getChildAt(i) as TextView
            child.visibility = View.VISIBLE
            child.text = bean.children[i].name
        }
    }

    private fun getChildView(): TextView {
        val inflater = LayoutInflater.from(context)
        return inflater.inflate(R.layout.item_system_tree_list_adapter2, this, false) as TextView
    }

    fun getViewHeight(): Int {

        if (childCount == 0) {
            return 0
        }

        Debug.info(TAG, "TreeListContentView getViewHeight numRows=$numRows")

        var num = 1
        var viewNum = 0
        for (row in 0 until numRows) {
            val columns = getNumColumns(row)
            Debug.info(TAG, "TreeListContentView getViewHeight row=$row columns=$columns")

            viewNum += columns

            if (viewNum + 1 >= childCount) {
                break
            }

            val childView = getChildAt(viewNum + 1)
            if (childView.visibility == View.GONE) {
                break
            }

            num++
        }

        Debug.info(TAG, "TreeListContentView getViewHeight num=$num viewNum=$viewNum")

        val child = getChildAt(0)
        val childHeight = Utils.getUnDisplayViewHeight(child)
        val viewTotalHeight = childHeight * num

        val layoutParams = layoutParams as LinearLayout.LayoutParams
        Debug.info(TAG, "TreeListContentView getViewHeight paddingBottom=$paddingBottom paddingTop=$paddingTop topMargin=${layoutParams.topMargin} bottomMargin=${layoutParams.bottomMargin} verticalSpacing=${verticalSpacing * (num - 1)}")
        return viewTotalHeight + paddingBottom + paddingTop + layoutParams.topMargin + layoutParams.bottomMargin + (verticalSpacing * (num - 1))
    }
}