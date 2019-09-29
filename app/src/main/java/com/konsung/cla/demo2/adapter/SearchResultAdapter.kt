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

class SearchResultAdapter(context: Context, data: List<HomeData.DatasBean>) : BasicDataQuickAdapter<BaseViewHolder>(context, R.layout.adapter_search_result, data) {

    companion object {
        val TAG: String = SearchResultAdapter::class.java.simpleName
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
            helper.setText(R.id.tvTitle, StringUtils.instance.formHtml(title))
                    .setText(R.id.tvAuthor, author)
                    .setText(R.id.tvChapter, StringUtils.instance.formHtml(getChapter(item)))
                    .setText(R.id.tvTime, niceDate)
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

