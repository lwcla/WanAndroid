package com.konsung.cla.demo2.adapter

import android.content.Context
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.konsung.basic.bean.project.ProjectTitleBean
import com.konsung.basic.util.StringUtils
import com.konsung.cla.demo2.R

/**
 * 公众号标题
 */
class WxArticleTitleAdapter(private val context: Context, dataList: List<ProjectTitleBean>, private val selectId: Int) : BaseQuickAdapter<ProjectTitleBean, BaseViewHolder>(R.layout.adapter_search_hot, dataList) {

    override fun convert(helper: BaseViewHolder, item: ProjectTitleBean?) {
        item?.apply {
            val tvName = helper.getView<TextView>(R.id.tvName)
            tvName.text = name

            if (selectId == id) {
                tvName.setTextColor(ContextCompat.getColor(context, R.color.white))
                tvName.isSelected = true
            } else {
                tvName.setTextColor(StringUtils.instance.intRandomColor())
                tvName.isSelected = false
            }
        }
    }
}