package com.konsung.basic.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.konsung.basic.bean.HomeData

interface BaseAdapterHelper {

    /**
     * 根据位置找到那条数据
     */
    fun findDataByPosition(position: Int): HomeData.DatasBean?

    /**
     * 刷新收藏状态
     *
     * @param position 收藏的位置
     * @param toCollect 是收藏还是取消收藏
     */
    fun refreshCollectStatus(position: Int, toCollect: Boolean)

    /**
     * 通过position找到图片地址
     */
    fun findImageByPosition(position: Int): String?

    /**
     * 通过Position找到id
     */
    fun findIdByPosition(position: Int): Int

    /**
     * 设置收藏状态
     */
    fun setCollectStatus(helper: BaseViewHolder, collect: Boolean)

    /**
     * 刷新收藏状态
     */
    fun refreshCollectStatus(position: Int, data: HomeData.DatasBean)

}