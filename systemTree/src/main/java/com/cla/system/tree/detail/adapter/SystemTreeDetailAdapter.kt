package com.cla.system.tree.detail.adapter

import android.content.Context
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseViewHolder
import com.cla.system.tree.R
import com.konsung.basic.adapter.BasicDataQuickAdapter
import com.konsung.basic.bean.HomeData
import com.konsung.basic.util.StringUtils

class SystemTreeDetailAdapter(context: Context, data: List<HomeData.DatasBean>) : BasicDataQuickAdapter<BaseViewHolder>(context, R.layout.adapter_system_tree_detail, data) {

    companion object {
        val TAG: String = SystemTreeDetailAdapter::class.java.simpleName
    }


    override fun getStartImvId(): Int = R.id.imvStart

    override fun convert(helper: BaseViewHolder, item: HomeData.DatasBean?) {
        item?.apply {
            helper.setText(R.id.tvTitle, StringUtils.instance.formHtml(title))
                    .setText(R.id.tvAuthor, author)
                    .setText(R.id.tvTime, context.getString(R.string.date) + niceDate)
                    .addOnClickListener(R.id.imvStart)

            setCollectStatus(helper, collect)

            val tvDesc = helper.getView<TextView>(R.id.tvDesc)
            val description = StringUtils.instance.clearNull(desc)
            if (description.isEmpty()) {
                tvDesc.visibility = View.GONE
            } else {
                tvDesc.visibility = View.VISIBLE
                tvDesc.text = StringUtils.instance.formHtml(description)
            }
        }
    }
}

