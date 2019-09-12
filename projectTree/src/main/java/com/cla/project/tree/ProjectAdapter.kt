package com.cla.project.tree

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.konsung.basic.bean.HomeData
import com.konsung.basic.config.ImageLoadUtil
import com.konsung.basic.util.StringUtils
import com.konsung.basic.util.toast

class ProjectAdapter(private val context: Context, dataList: List<HomeData.DatasBean>) : BaseQuickAdapter<HomeData.DatasBean, BaseViewHolder>(R.layout.item_project_adapter, dataList) {

    companion object {
        val TAG: String = ProjectAdapter::class.java.simpleName

        const val COLLECT_STATUS_CHANGE = "collectStatus"
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

    override fun convert(helper: BaseViewHolder, item: HomeData.DatasBean?) {
        item?.apply {

            helper.setText(R.id.tvAuthor, author)
                    .setText(R.id.tvDate, niceDate)
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