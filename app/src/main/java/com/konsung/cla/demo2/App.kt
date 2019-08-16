package com.konsung.cla.demo2

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.konsung.basic.util.AppUtils
import com.konsung.cla.demo2.util.AppHelper
import com.konsung.cla.demo2.util.BasicConfigImpl
import com.konsung.cla.demo2.util.ProductUtils

class App : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        val productUtils: ProductUtils by lazy { AppHelper().getProductUtils() }
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        AppUtils.instance.init(BasicConfigImpl.instance)

    }

}