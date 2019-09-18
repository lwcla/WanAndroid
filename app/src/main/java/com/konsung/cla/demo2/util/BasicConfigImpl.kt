package com.konsung.cla.demo2.util

import android.app.Activity
import android.content.Context
import android.view.View
import com.konsung.basic.bean.tree.SystemTreeTitle
import com.konsung.basic.config.BasicHelper
import com.konsung.cla.demo2.App
import com.konsung.cla.demo2.BuildConfig

class BasicConfigImpl private constructor() : BasicHelper {
    companion object {

        val instance by lazy { BasicConfigImpl() }
    }

    override fun debug(): Boolean {
        return BuildConfig.DEBUG
    }

    override fun getContext(): Context = App.context

    override fun startLoginAty(context: Context?) {
        App.productUtils.startLoginAty(context)
    }

    override fun startScreenImageAty(activity: Activity?, url: String) {
        App.productUtils.startScreenImageAty(activity, url)
    }

    override fun startSystemTreeDetailAty(activity: Activity?, view: View, title: SystemTreeTitle) {
        App.productUtils.startSystemTreeDetailAty(activity, view, title)
    }

    override fun startWebAty(context: Context?, title: String?, link: String?, artId: Int, collect: Boolean, dataPosition: Int, needCollect: Boolean) {
        App.productUtils.startWebAty(context, title, link, artId, collect, dataPosition, needCollect)
    }

    override fun startWebAty(activity: Activity?, context: Context?, view: View, title: String?, link: String?, artId: Int, collect: Boolean, dataPosition: Int, needCollect: Boolean) {
        App.productUtils.startWebAty(activity, context, view, title, link, artId, collect, dataPosition, needCollect)
    }
}