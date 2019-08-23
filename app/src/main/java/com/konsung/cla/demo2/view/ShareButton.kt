package com.konsung.cla.demo2.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.LinearLayout
import android.widget.TextView
import com.konsung.basic.util.StringUtils
import com.konsung.cla.demo2.R
import kotlinx.android.synthetic.main.button_share.view.*

class ShareButton(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    init {

        this.orientation = VERTICAL
        this.gravity = Gravity.CENTER

        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.button_share, this, true)
        val tvIcon = view.findViewById<TextView>(R.id.tvIcon)
        val tvName = view.findViewById<TextView>(R.id.tvName)
        StringUtils.instance.loadTextIcon(context, tvIcon)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShareButton)
        val icon = typedArray.getString(R.styleable.ShareButton_iconString)
        val name = typedArray.getString(R.styleable.ShareButton_name)
        typedArray.recycle()

        tvIcon.text = icon
        tvName.text = name

        llContent.setOnTouchListener { _, ev ->
            when (ev.action) {
                MotionEvent.ACTION_DOWN -> tvIcon.setBackgroundResource(R.drawable.bg_grey_round)
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> tvIcon.setBackgroundResource(R.drawable.bg_white_round)
            }

            return@setOnTouchListener false
        }
    }
}