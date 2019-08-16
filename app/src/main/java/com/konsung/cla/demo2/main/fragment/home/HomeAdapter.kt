package com.konsung.cla.demo2.main.fragment.home

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.konsung.basic.bean.HomeData
import com.konsung.basic.util.DateUtiils
import com.konsung.basic.util.ImageLoad
import com.konsung.cla.demo2.R

class HomeAdapter(private val context: Context, data: List<HomeData.DatasBean>) : BaseQuickAdapter<HomeData.DatasBean, BaseViewHolder>(R.layout.view_home_adapter, data) {

    override fun convert(helper: BaseViewHolder, item: HomeData.DatasBean) {
        item.apply {
            helper.setText(R.id.tvTitle, title?.trim())
            helper.setText(R.id.tvAuthor, author)
            helper.setText(R.id.tvChapter, "$superChapterName/$chapterName")
            helper.setText(R.id.tvTime, DateUtiils.convertTime(publishTime))

            helper.setImageResource(R.id.imvStart, if (isCollect) {
                R.mipmap.start
            } else {
                R.mipmap.start_off
            })

            helper.setText(R.id.tvNiceNum, zan.toString())
            val imvNice = helper.getView<ImageView>(R.id.imvNice)
            if (zan == 0) {
                imvNice.setImageResource(R.mipmap.nice_off)
            } else {
                imvNice.setImageResource(R.mipmap.nice)
            }

            val imvHead = helper.getView<ImageView>(R.id.imvEnvelopePic)
            if (envelopePic.isNullOrEmpty()) {
                imvHead.visibility = View.GONE
            } else {
                imvHead.visibility = View.VISIBLE
                ImageLoad.into(context, envelopePic!!, imvHead)
            }

        }

    }
}

