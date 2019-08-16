package com.konsung.basic.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.konsung.basic.util.R
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.ClassicsFooter

class RefreshRecyclerView : SmartRefreshLayout {

    companion object {
        val TAG = RefreshRecyclerView::class.java.simpleName
    }

    var rv: RecyclerView

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
}
