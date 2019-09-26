package com.cla.wx.article.adapter

import android.content.Context
import com.chad.library.adapter.base.BaseViewHolder
import com.cla.wx.article.R
import com.konsung.basic.adapter.BasicDataQuickAdapter
import com.konsung.basic.bean.HomeData
import com.konsung.basic.util.StringUtils

class WxDetailAdapter(context: Context, dataList: List<HomeData.DatasBean>) : BasicDataQuickAdapter<BaseViewHolder>(context, R.layout.adapter_wx_detail, dataList) {

    companion object {
        val TAG: String = WxDetailAdapter::class.java.simpleName
    }

    override fun getStartImvId(): Int = R.id.imvStart

    override fun convert(helper: BaseViewHolder, item: HomeData.DatasBean?) {
        item?.apply {
            helper.setText(R.id.tvTitle, StringUtils.instance.formHtml(title))
                    .setText(R.id.tvTime, context.getString(R.string.date) + niceDate)
                    .addOnClickListener(R.id.imvStart)

            setCollectStatus(helper, collect)
        }
    }
}