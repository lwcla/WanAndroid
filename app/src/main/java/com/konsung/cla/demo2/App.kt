package com.konsung.cla.demo2

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.konsung.basic.net.NetworkStatusCallback
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
        AppUtils.init(BasicConfigImpl.instance)
        NetworkStatusCallback.registerNetwork(this)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        //在系统内存不足，所有后台程序（优先级为background的进程，不是指后台运行的进程）都被杀死时，系统会调用OnLowMemory。
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        //是Android 4.0之后提供的API，系统会根据不同的内存状态来回调。根据不同的内存状态，来响应不同的内存释放策略。
    }

}