package com.konsung.basic.config

import android.app.Activity
import android.content.Context
import android.view.View

interface BasicHelper {

    fun debug(): Boolean

    fun getContext(): Context

    fun startLoginAty(context: Context?)

    fun startScreenImageAty(context: Context, url: String)

    fun startWebAty(activity: Activity?, context: Context?, view: View, title: String?, link: String?, artId: Int, collect: Boolean, dataPosition: Int = -1, needCollect: Boolean = true)
}