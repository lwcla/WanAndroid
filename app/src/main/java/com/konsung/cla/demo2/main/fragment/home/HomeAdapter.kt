package com.konsung.cla.demo2.main.fragment.home

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.konsung.basic.bean.HomeData
import com.konsung.basic.util.DateUtils
import com.konsung.basic.util.ImageLoad
import com.konsung.cla.demo2.R

class HomeAdapter(private val context: Context, data: List<HomeData.DatasBean>) : BaseQuickAdapter<HomeData.DatasBean, BaseViewHolder>(R.layout.view_home_adapter, data) {

    companion object {
        val TAG: String = HomeAdapter::class.java.simpleName
    }

    override fun convert(helper: BaseViewHolder, item: HomeData.DatasBean) {
        item.apply {
            helper.setText(R.id.tvTitle, title?.trim())
                    .setText(R.id.tvAuthor, author)
                    .setText(R.id.tvChapter, "$superChapterName/$chapterName")
                    .setText(R.id.tvTime, DateUtils.fromToday(publishTime))
                    .setText(R.id.tvNiceNum, zan.toString())
                    .setImageResource(R.id.imvStart, if (isCollect) {
                        R.mipmap.start
                    } else {
                        R.mipmap.start_off
                    })
                    .setImageResource(R.id.imvNice, if (zan == 0) {
                        R.mipmap.nice_off
                    } else {
                        R.mipmap.nice
                    })
                    .addOnClickListener(R.id.imvEnvelopePic, R.id.imvStart, R.id.llNice)
//                    .addOnClickListener(R.id.clContent)
//                    .addOnClickListener(R.id.imvStart)
//                    .addOnClickListener(R.id.llNice)


            /*    val imvStart = helper.getView<ImageView>(R.id.imvStart)
                imvStart.setOnClickListener {
                    context.toast(TAG, "点击收藏")
                }

                val imvNice = helper.getView<ImageView>(R.id.imvNice)
                imvNice.setOnClickListener {
                    context.toast(TAG, "点击喜欢")
                }*/

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

