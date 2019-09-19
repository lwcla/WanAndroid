package com.cla.wx.article.adapter

import android.content.Context
import com.chad.library.adapter.base.BaseViewHolder
import com.cla.wx.article.R
import com.konsung.basic.adapter.BasicDataQuickAdapter
import com.konsung.basic.bean.HomeData
import com.konsung.basic.util.StringUtils
import com.konsung.basic.util.toast

class WxDetailAdapter(private val context: Context, dataList: List<HomeData.DatasBean>) : BasicDataQuickAdapter<HomeData.DatasBean, BaseViewHolder>(R.layout.adapter_wx_detail, dataList) {

    companion object {
        val TAG: String = WxDetailAdapter::class.java.simpleName

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
                    .setText(R.id.tvTime, context.getString(R.string.date) + niceDate)
                    .addOnClickListener(R.id.imvStart)

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