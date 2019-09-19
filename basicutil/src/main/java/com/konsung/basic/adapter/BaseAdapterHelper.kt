package com.konsung.basic.adapter

interface BaseAdapterHelper {

    /**
     * 刷新收藏状态
     *
     * @param position 收藏的位置
     * @param toCollect 是收藏还是取消收藏
     */
    fun refreshCollectStatus(position: Int, toCollect: Boolean)

}