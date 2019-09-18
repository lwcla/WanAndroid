package com.cla.home.main

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cla.home.R
import com.konsung.basic.bean.HomeData
import com.konsung.basic.config.ImageLoadUtil
import com.konsung.basic.util.DateUtils
import com.konsung.basic.util.StringUtils
import com.konsung.basic.util.toast

class HomeAdapter(private val context: Context, data: List<HomeData.DatasBean>) : BaseQuickAdapter<HomeData.DatasBean, BaseViewHolder>(R.layout.adapter_view_home, data) {

    companion object {
        val TAG: String = HomeAdapter::class.java.simpleName

        const val COLLECT_STATUS_CHANGE = "collectStatus"
    }

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

    /**
     * 设置收藏状态
     */
    private fun setCollectStatus(helper: BaseViewHolder, collect: Boolean) {

        val imageId = if (collect) {
            R.mipmap.start
        } else {
            R.mipmap.start_off
        }

        helper.setImageResource(R.id.imvStart, imageId)
    }

    /**
     * 刷新收藏状态
     */
    fun refreshCollectStatus(position: Int, toCollect: Boolean) {

        val data = findDataByPosition(position)
        if (data == null) {
            context.toast(TAG, R.string.data_error)
            return
        }

        val id = data.id
        if (id < 0) {
            context.toast(TAG, R.string.data_error)
            return
        }
        data.collect = toCollect

        notifyItemChanged(position + headerLayoutCount, COLLECT_STATUS_CHANGE)
    }

    /**
     * 刷新收藏状态
     */
    fun refreshCollectStatus(position: Int, data: HomeData.DatasBean) {
        //先把状态设置为收藏成功，如果收藏失败的话，再改回来
        val collectFlag: Boolean = data.collect
        data.collect = !collectFlag

        notifyItemChanged(position + headerLayoutCount, COLLECT_STATUS_CHANGE)
    }

    override fun getItemId(position: Int): Long = position.toLong()

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

    override fun convertPayloads(helper: BaseViewHolder, item: HomeData.DatasBean?, payloads: MutableList<Any>) {

        if (item == null) {
            return
        }

        if (payloads.isEmpty()) {
            convert(helper, item)
        } else {

            if (payloads[0] == COLLECT_STATUS_CHANGE) {
                setCollectStatus(helper, item.collect)
            }
        }
    }

}

