package com.konsung.cla.demo2.main.fragment.home

import android.content.Context
import android.os.Build
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.konsung.basic.bean.HomeData
import com.konsung.basic.util.DateUtils
import com.konsung.basic.util.ImageLoad
import com.konsung.basic.util.StringUtils
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
                    .setText(R.id.tvNiceNum, "$zan  ${context.getString(R.string.agree)}")
                    .setImageResource(R.id.imvStart, if (isCollect) {
                        R.mipmap.start
                    } else {
                        R.mipmap.start_off
                    })
                    .addOnClickListener(R.id.imvEnvelopePic, R.id.imvStart)

            //无法知道自己是否已经赞过
            /*   .setImageResource(R.id.imvNice, if (zan == 0) {
                   R.mipmap.nice_off
               } else {
                   R.mipmap.nice
               })*/

            val tvDesc = helper.getView<TextView>(R.id.tvDesc)
            val description = StringUtils.instance.clearNull(desc)
            if (description.isEmpty()) {
                tvDesc.visibility = View.GONE
            } else {
                tvDesc.visibility = View.VISIBLE
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    tvDesc.text = Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT)
                } else {
                    tvDesc.text = Html.fromHtml(description)
                }
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

    override fun getItemId(position: Int): Long = position.toLong()
}

