package com.konsung.cla.demo2.adapter

import android.content.Context
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.konsung.basic.bean.search.SearchKey
import com.konsung.basic.util.StringUtils
import com.konsung.cla.demo2.R

class SearchKeyAdapter(private val context: Context, dataList: List<SearchKey>, private val isHotKey: Boolean) : BaseQuickAdapter<SearchKey, BaseViewHolder>(R.layout.adapter_search_hot, dataList) {

    override fun convert(helper: BaseViewHolder, item: SearchKey?) {
        item?.apply {
            val tvName = helper.getView<TextView>(R.id.tvName)
            tvName.text = name

            if (isHotKey) {
                tvName.setTextColor(StringUtils.instance.intRandomColor())
            } else {
                tvName.setTextColor(ContextCompat.getColor(context, R.color.value_default))
            }

            helper.addOnClickListener(R.id.tvName)
        }
    }
}