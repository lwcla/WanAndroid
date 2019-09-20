package com.cla.system.tree.view

import am.widget.wraplayout.WrapLayout
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.View.MeasureSpec.UNSPECIFIED
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.cla.system.tree.R
import com.konsung.basic.bean.TwoBean
import com.konsung.basic.bean.tree.SystemTreeListBean
import com.konsung.basic.util.Debug

class TreeListContentView(context: Context, attrs: AttributeSet) : WrapLayout(context, attrs) {

    companion object {
        val TAG: String = TreeListContentView::class.java.simpleName
    }

    var itemClickListener: OnClickListener? = null

    private val space: Int = context.resources.getDimension(R.dimen.dp_7).toInt()

    var contentWidth: Int = 0

    init {
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
                val child = TreeListTextView(context)
                child.listener = itemClickListener
                child.visibility = View.VISIBLE
                addView(child)
            }
        }

        for (i in 0 until dataSize) {
            val child = getChildAt(i) as TreeListTextView
            child.visibility = View.VISIBLE
            child.bind(bean.children[i], i)
        }
    }

    /**
     * 计算view的高度
     *
     * @return TwoBean(view的高度,总共几行)
     */
    fun getViewHeight(): TwoBean<Int, Int> {

        var itemWidths = 0
        val viewWidth = contentWidth - paddingStart - paddingEnd
        var itemHeight = 0
        val itemHeightList = mutableListOf<Int>()
        var columnsNum = 0

//        Debug.info(TAG, "TreeListContentView measure paddingStart=$paddingStart itemWidths=$itemWidths viewWidth=$viewWidth")

        var i = 0
        while (i in 0 until childCount) {

//            Debug.info(TAG, "TreeListContentView measure i=$i childCount=$childCount")

            val child = getChildAt(i) as TextView
            if (child.visibility == View.GONE) {
                i++
                continue
            }

            measureChild(child, UNSPECIFIED, UNSPECIFIED)

            val h = child.measuredHeight
            itemHeight = if (h > itemHeight) {
                h
            } else {
                itemHeight
            }

//            Debug.info(TAG, "TreeListContentView measure text=${child.text} child.width=${child.measuredWidth}")

            itemWidths += if (columnsNum == 0) {
                child.measuredWidth
            } else {
                space + child.measuredWidth
            }

//            Debug.info(TAG, "TreeListContentView measure itemWidths=$itemWidths viewWidth=$viewWidth ")

            if (itemWidths > viewWidth) {
                Debug.info(TAG, "TreeListContentView getViewHeight 计算下一行")
                itemHeightList.add(itemHeight)
                itemWidths = 0
                itemHeight = 0
                columnsNum = 0
                continue
            }

            i++
            columnsNum++
        }

        //最后一行在这里添加到集合中
        itemHeightList.add(itemHeight)

        var viewHeight = paddingTop + paddingBottom
        val lineNum = itemHeightList.size

        for (n in 0 until lineNum) {

//            Debug.info(TAG, "TreeListContentView getViewHeight 每一行的高度 i=$i height=${itemHeightList[n]}")

            viewHeight += if (n == 0) {
                itemHeightList[0]
            } else {
                space + itemHeightList[n]
            }
        }

        Debug.info(TAG, "TreeListContentView measure viewHeight=$viewHeight 总共有$lineNum 行")

        return TwoBean(viewHeight, lineNum)
    }
}

class TreeListTextView(context: Context) : TextView(context) {

    private var bean: SystemTreeListBean? = null
    private var position: Int = -1

    var listener: OnClickListener? = null

    init {
        layoutParams = ViewGroup.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        val padding = context.resources.getDimension(R.dimen.dp_5).toInt()
        setPadding(padding, padding, padding, 0)
        setTextColor(context.resources.getColorStateList(R.color.color_text_name))
        textSize = 14.toFloat()
        id = R.id.tvName

        setOnClickListener {
            if (bean == null) {
                return@setOnClickListener
            }

            it.setTag(R.id.text_view_click_data, bean)
            it.setTag(R.id.text_view_click_position, position)
            listener?.onClick(it)
        }
    }

    fun bind(bean: SystemTreeListBean, position: Int) {
        this.bean = bean
        this.position = position

        text = bean.name
    }

}