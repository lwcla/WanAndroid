package com.konsung.basic.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

abstract class BasicDataQuickAdapter<T, K : BaseViewHolder>(layoutRes: Int, dataList: List<T>) : BaseQuickAdapter<T, K>(layoutRes, dataList), BaseAdapterHelper {

}