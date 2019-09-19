package com.cla.system.tree.detail.adapter

import android.content.Context
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseViewHolder
import com.cla.system.tree.R
import com.konsung.basic.adapter.BasicDataQuickAdapter
import com.konsung.basic.bean.HomeData
import com.konsung.basic.util.StringUtils
import com.konsung.basic.util.toast

class SystemTreeDetailAdapter(private val context: Context, data: List<HomeData.DatasBean>) : BasicDataQuickAdapter<HomeData.DatasBean, BaseViewHolder>(R.layout.adapter_system_tree_detail, data) {

    companion object {
        val TAG: String = SystemTreeDetailAdapter::class.java.simpleName

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
    override fun refreshCollectStatus(position: Int, toCollect: Boolean) {

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
                    .setText(R.id.tvTime, context.getString(R.string.date) + niceDate)
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

