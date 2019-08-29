package com.konsung.basic.ui

import android.view.View
import android.view.ViewGroup

class HeightView(private val view: View) {

    var viewHeight: Int
        get() = 0
        set(value) {
            val layoutParams: ViewGroup.LayoutParams? = view.layoutParams
            layoutParams?.let {
                it.height = value
                view.layoutParams = it
            }
        }
}