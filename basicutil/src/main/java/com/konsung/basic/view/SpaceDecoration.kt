package com.konsung.basic.view

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * 用空格来做分割线
 * @param space 空格的高度
 * @param hasHead 是否有headView，如果有的话，那么headView和正式的item之间也不能有空格
 */
class SpaceDecoration(private val space: Int = 0, private val hasHead: Boolean = false) : RecyclerView.ItemDecoration() {

    companion object {
        val TAG: String = SpaceDecoration::class.java.simpleName
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
//        Debug.info(TAG, "SpaceDecoration getItemOffsets space=$space")
        val position = parent.getChildAdapterPosition(view)
        if (position == 0) {
            if (hasHead) {
                outRect.top = 0
            } else {
                outRect.top = space
            }
        }

        outRect.bottom = space
    }

}