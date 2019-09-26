package com.cla.home.main

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseViewHolder
import com.cla.home.R
import com.konsung.basic.adapter.BasicDataQuickAdapter
import com.konsung.basic.bean.HomeData
import com.konsung.basic.config.ImageLoadUtil
import com.konsung.basic.util.DateUtils
import com.konsung.basic.util.StringUtils

class HomeAdapter(context: Context, data: List<HomeData.DatasBean>) : BasicDataQuickAdapter<BaseViewHolder>(context, R.layout.adapter_view_home, data) {

    companion object {
        val TAG: String = HomeAdapter::class.java.simpleName
    }

    override fun getStartImvId(): Int = R.id.imvStart

    override fun convert(helper: BaseViewHolder, item: HomeData.DatasBean?) {
        item?.apply {
            helper.setText(R.id.tvTitle, StringUtils.instance.formHtml(title))
                    .setText(R.id.tvAuthor, author)
                    .setText(R.id.tvChapter, "$superChapterName/$chapterName")
                    .setText(R.id.tvTime, DateUtils.fromToday(publishTime))
                    .setText(R.id.tvNiceNum, "$zan  ${context.getString(R.string.agree)}")
                    .addOnClickListener(R.id.imvEnvelopePic, R.id.imvStart)

            //无法知道自己是否已经赞过
            /*   .setImageResource(R.id.imvNice, if (zan == 0) {
                   R.mipmap.nice_off
               } else {
                   R.mipmap.nice
               })*/

            setCollectStatus(helper, collect)

            val tvDesc = helper.getView<TextView>(R.id.tvDesc)
            val description = StringUtils.instance.clearNull(desc)
            if (description.isEmpty()) {
                tvDesc.visibility = View.GONE
            } else {
                tvDesc.visibility = View.VISIBLE
                tvDesc.text = StringUtils.instance.formHtml(description)
            }

            val imvHead = helper.getView<ImageView>(R.id.imvEnvelopePic)
            if (envelopePic.isNullOrEmpty()) {
                imvHead.visibility = View.GONE
            } else {
                imvHead.visibility = View.VISIBLE
                ImageLoadUtil.imageLoad.into(context, envelopePic!!, imvHead)
            }

            helper.getView<TextView>(R.id.tvTop).visibility = if (type == 1) View.VISIBLE else View.GONE
            helper.getView<TextView>(R.id.tvFresh).visibility = if (fresh) View.VISIBLE else View.GONE

            var tag = ""
            if (tags?.size ?: 0 > 0) {
                tag = tags?.get(0)?.name ?: ""
            }

            val tvTag = helper.getView<TextView>(R.id.tvTag)
            if (tag.isNotEmpty()) {
                tvTag.visibility = View.VISIBLE
                tvTag.text = tags?.get(0)?.name
            } else {
                tvTag.visibility = View.GONE
            }
        }
    }
}

