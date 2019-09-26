package com.konsung.basic.adapter

import android.content.Context
import androidx.annotation.IdRes
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.konsung.basic.bean.HomeData
import com.konsung.basic.util.R
import com.konsung.basic.util.StringUtils
import com.konsung.basic.util.toast

abstract class BasicDataQuickAdapter<K : BaseViewHolder>(val context: Context, layoutRes: Int, dataList: List<HomeData.DatasBean>) :
        BaseQuickAdapter<HomeData.DatasBean, K>(layoutRes, dataList), BaseAdapterHelper {

    companion object {
        const val COLLECT_STATUS_CHANGE = "collectStatus"
    }

    override fun findDataByPosition(position: Int): HomeData.DatasBean? {

        val size = data.size
        if (position >= size) {
            return null
        }

        return data[position]
    }

    /**
     * 通过position找到图片地址
     */
    override fun findImageByPosition(position: Int): String? {

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
    override fun findIdByPosition(position: Int): Int {
        return findDataByPosition(position)?.chapterId ?: -1
    }

    /**
     * 设置收藏状态
     */
    override fun setCollectStatus(helper: BaseViewHolder, collect: Boolean) {

        val imageId = if (collect) {
            R.mipmap.start
        } else {
            R.mipmap.start_off
        }

        helper.setImageResource(getStartImvId(), imageId)
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
    override fun refreshCollectStatus(position: Int, data: HomeData.DatasBean) {
        //先把状态设置为收藏成功，如果收藏失败的话，再改回来
        val collectFlag: Boolean = data.collect
        data.collect = !collectFlag

        notifyItemChanged(position + headerLayoutCount, COLLECT_STATUS_CHANGE)
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun convertPayloads(helper: K, item: HomeData.DatasBean?, payloads: MutableList<Any>) {
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

    @IdRes
    abstract fun getStartImvId(): Int
}