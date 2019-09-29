package com.konsung.cla.demo2.adapter

import android.content.Context
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseViewHolder
import com.konsung.basic.adapter.BasicDataQuickAdapter
import com.konsung.basic.bean.HomeData
import com.konsung.basic.util.StringUtils
import com.konsung.cla.demo2.R
import java.util.*

class CollectListAdapter(context: Context, data: List<HomeData.DatasBean>) : BasicDataQuickAdapter<BaseViewHolder>(context, R.layout.adapter_search_result, data) {

    companion object {
        val TAG: String = CollectListAdapter::class.java.simpleName
    }

    private fun getChapter(item: HomeData.DatasBean?): String {
        if (item == null) {
            return ""
        }

        var chapter = ""
        val superChapterName = item.superChapterName
        if (!superChapterName.isNullOrEmpty() && superChapterName.toUpperCase(Locale.CHINA) != "null".toUpperCase(Locale.CHINA)) {
            chapter = "$superChapterName/"
        }

        return chapter + item.chapterName
    }

    override fun getStartImvId(): Int = R.id.imvStart

    override fun convert(helper: BaseViewHolder, item: HomeData.DatasBean?) {
        item?.apply {

            val chapter: String
            val name: String

            //收藏的站外文章 originId ==-1
            if (originId == -1) {
                val t = StringUtils.instance.formHtml(title)

                chapter = t
                name = link ?: t
            } else {
                chapter = StringUtils.instance.formHtml(getChapter(item))
                name = StringUtils.instance.formHtml(title)
            }

            helper.setText(R.id.tvTitle, name)
                    .setText(R.id.tvAuthor, author)
                    .setText(R.id.tvChapter, chapter)
                    .setText(R.id.tvTime, niceDate)
                    .addOnClickListener(R.id.imvStart)

            setCollectStatus(helper, collect)

            val description = StringUtils.instance.clearNull(desc)

            val tvDesc = helper.getView<TextView>(R.id.tvDesc)
            if (description.isEmpty()) {
                tvDesc.visibility = View.GONE
            } else {
                tvDesc.visibility = View.VISIBLE
                tvDesc.text = StringUtils.instance.formHtml(description)
            }
        }
    }
}

