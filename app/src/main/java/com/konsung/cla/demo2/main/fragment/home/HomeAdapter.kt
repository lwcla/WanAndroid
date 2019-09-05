package com.konsung.cla.demo2.main.fragment.home

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.konsung.basic.bean.HomeData
import com.konsung.basic.config.ImageLoadUtil
import com.konsung.basic.util.DateUtils
import com.konsung.basic.util.StringUtils
import com.konsung.cla.demo2.R

class HomeAdapter(private val context: Context, data: List<HomeData.DatasBean>) : BaseQuickAdapter<HomeData.DatasBean, BaseViewHolder>(R.layout.view_home_adapter, data) {

    companion object {
        val TAG: String = HomeAdapter::class.java.simpleName
    }

    override fun convert(helper: BaseViewHolder, item: HomeData.DatasBean) {
        item.apply {
            helper.setText(R.id.tvTitle, StringUtils.instance.formHtml(title))
                    .setText(R.id.tvAuthor, author)
                    .setText(R.id.tvChapter, "$superChapterName/$chapterName")
                    .setText(R.id.tvTime, DateUtils.fromToday(publishTime))
                    .setText(R.id.tvNiceNum, "$zan  ${context.getString(R.string.agree)}")
                    .setImageResource(R.id.imvStart, if (collect) {
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
            if (!tag.isEmpty()) {
                tvTag.visibility = View.VISIBLE
                tvTag.text = tags?.get(0)?.name
            } else {
                tvTag.visibility = View.GONE
            }
        }
    }

    override fun getItemId(position: Int): Long = position.toLong()

    fun findDataByPosition(position: Int): HomeData.DatasBean? {

        val size = data.size
        if (position >= size) {
            return null
        }

        return data[position]
    }

    /**
     * 通过position找到图片地址
     */
    fun findImageByPosition(position: Int): String? {

        val url = StringUtils.instance.clearNull(findDataByPosition(position)?.envelopePic)
        return if (url.isEmpty()) {
            null
        } else {
            url
        }
    }

    /**
     * 通过Position找到id
     */
    fun findIdByPosition(position: Int): Int {
        return findDataByPosition(position)?.chapterId ?: -1
    }
}

