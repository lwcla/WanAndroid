package com.konsung.basic.config

import android.app.Activity
import android.content.Context
import android.view.View
import com.konsung.basic.bean.tree.SystemTreeTitle

interface BasicHelper {

    fun debug(): Boolean

    fun getContext(): Context

    fun startLoginAty(context: Context?)

    fun startScreenImageAty(activity: Activity?, url: String)

    fun startSystemTreeDetailAty(activity: Activity?, view: View, title: SystemTreeTitle)

    fun startWebAty(context: Context?, title: String?, link: String?, artId: Int, collect: Boolean, dataPosition: Int = -1, needCollect: Boolean = true)

    fun startWebAty(activity: Activity?, context: Context?, view: View, title: String?, link: String?, artId: Int, collect: Boolean, dataPosition: Int = -1, needCollect: Boolean = true)
}