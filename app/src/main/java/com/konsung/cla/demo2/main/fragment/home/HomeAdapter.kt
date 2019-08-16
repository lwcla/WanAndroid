package com.konsung.cla.demo2.main.fragment.home

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.konsung.basic.bean.HomeData
import com.konsung.basic.util.DateUtiils
import com.konsung.cla.demo2.R

class HomeAdapter(data: List<HomeData.DatasBean>) : BaseQuickAdapter<HomeData.DatasBean, BaseViewHolder>(R.layout.view_home_adapter, data) {

    override fun convert(helper: BaseViewHolder, item: HomeData.DatasBean) {
        helper.setText(R.id.tvTitle, item.title?.trim())
        helper.setText(R.id.tvAuthor, item.author)
        helper.setText(R.id.tvTime, DateUtiils.convertTime(item.publishTime))
    }
}

