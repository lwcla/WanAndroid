package com.konsung.cla.demo2.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.konsung.basic.bean.search.SearchHotKey
import com.konsung.basic.util.StringUtils
import com.konsung.cla.demo2.R

class HotSearchAdapter(dataList: List<SearchHotKey>) : BaseQuickAdapter<SearchHotKey, BaseViewHolder>(R.layout.adapter_search_hot, dataList) {

    override fun convert(helper: BaseViewHolder, item: SearchHotKey?) {
        item?.apply {
            val tvName = helper.getView<TextView>(R.id.tvName)
            tvName.text = name
            tvName.setTextColor(StringUtils.instance.intRandomColor())

            helper.addOnClickListener(R.id.tvName)
        }
    }
}