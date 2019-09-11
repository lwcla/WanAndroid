package com.konsung.cla.demo2.main.fragment.common

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.konsung.basic.bean.CommonWebBean
import com.konsung.basic.util.StringUtils
import com.konsung.cla.demo2.R

class CommonWebAdapter(dataList: List<CommonWebBean>) : BaseQuickAdapter<CommonWebBean, BaseViewHolder>(R.layout.item_common_web_adapter, dataList) {

    override fun convert(helper: BaseViewHolder, item: CommonWebBean?) {

        item?.apply {
            val textView = helper.getView<TextView>(R.id.tvText)
            textView.setTextColor(StringUtils.instance.intrandomColor())
            textView.text = name

            helper.addOnClickListener(R.id.tvText)
        }
    }
}