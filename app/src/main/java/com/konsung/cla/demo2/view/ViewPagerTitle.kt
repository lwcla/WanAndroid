package com.konsung.cla.demo2.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import com.konsung.basic.util.StringUtils
import com.konsung.cla.demo2.R
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView

@SuppressLint("ViewConstructor")
class ViewPagerTitle(context: Context, @StringRes private val stringRes: Int, text: String) : CommonPagerTitleView(context) {

    companion object {
        val TAG = ViewPagerTitle::class.java.simpleName
    }

    private var customLayout: View = LayoutInflater.from(context).inflate(R.layout.view_pager_title, null)
    var tvName: TextView
    private var tvIcon: TextView
    private var currentText: Int? = null

    init {
        tvName = customLayout.findViewById(R.id.title_text) as TextView
        tvIcon = customLayout.findViewById(R.id.tvIcon) as TextView
        StringUtils.instance.loadTextIcon(context, tvIcon)
        initIcon()
        tvName.text = text
        setContentView(customLayout)

        onPagerTitleChangeListener = object : OnPagerTitleChangeListener {

            override fun onSelected(index: Int, totalCount: Int) {
                tvName.setTextColor(Color.BLACK)
                tvIcon.setTextColor(Color.BLACK)
            }

            override fun onDeselected(index: Int, totalCount: Int) {
                tvName.setTextColor(Color.LTGRAY)
                tvIcon.setTextColor(Color.LTGRAY)
            }

            override fun onLeave(index: Int, totalCount: Int, leavePercent: Float, leftToRight: Boolean) {
                customLayout.scaleX = 1.1f + (0.8f - 1.1f) * leavePercent
                customLayout.scaleY = 1.1f + (0.8f - 1.1f) * leavePercent
            }

            override fun onEnter(index: Int, totalCount: Int, enterPercent: Float, leftToRight: Boolean) {
                customLayout.scaleX = 0.8f + (1.1f - 0.8f) * enterPercent
                customLayout.scaleY = 0.8f + (1.1f - 0.8f) * enterPercent
            }
        }
    }

    @SuppressLint("ResourceType")
    fun initIcon() {
        setIcon(stringRes)
        tvName.visibility = View.VISIBLE
    }

    fun setIcon(@StringRes textRes: Int) {
//        Debug.info(TAG, "ViewPagerTitle setIcon currentText=$currentText textRes=$textRes currentText==textRes?${currentText == textRes}")
        if (currentText == textRes) {
            return
        }
        currentText = textRes
        tvIcon.setText(textRes)
    }
}