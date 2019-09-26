package com.cla.project.tree

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseViewHolder
import com.konsung.basic.adapter.BasicDataQuickAdapter
import com.konsung.basic.bean.HomeData
import com.konsung.basic.config.ImageLoadUtil
import com.konsung.basic.util.StringUtils

class ProjectAdapter(context: Context, dataList: List<HomeData.DatasBean>) : BasicDataQuickAdapter<BaseViewHolder>(context, R.layout.item_project_adapter, dataList) {

    companion object {
        val TAG: String = ProjectAdapter::class.java.simpleName
    }

    override fun getStartImvId(): Int = R.id.imvStart

    override fun convert(helper: BaseViewHolder, item: HomeData.DatasBean?) {
        item?.apply {

            helper.setText(R.id.tvAuthor, author)
                    .setText(R.id.tvDate, context.getString(R.string.date) + niceDate)
                    .setText(R.id.tvTitle, StringUtils.instance.formHtml(title))
                    .addOnClickListener(R.id.imvEnvelope, R.id.imvStart)

            val tvDesc = helper.getView<TextView>(R.id.tvDesc)
            val description = StringUtils.instance.clearNull(desc)
            if (description.isEmpty()) {
                tvDesc.visibility = View.GONE
            } else {
                tvDesc.visibility = View.VISIBLE
                tvDesc.text = StringUtils.instance.formHtml(description)
            }

            val imvHead = helper.getView<ImageView>(R.id.imvEnvelope)
            ImageLoadUtil.imageLoad.into(context, envelopePic!!, imvHead)

            setCollectStatus(helper, collect)

        }
    }
}