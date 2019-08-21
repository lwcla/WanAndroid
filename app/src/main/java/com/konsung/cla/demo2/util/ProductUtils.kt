package com.konsung.cla.demo2.util

import android.content.Context
import android.content.Intent
import com.konsung.cla.demo2.main.MainActivity
import com.konsung.cla.demo2.web.WebViewAty

open class ProductUtils {

    open fun startMainAty(context: Context) {
        val intent = Intent();
        intent.setClass(context, MainActivity::class.java)
        context.startActivity(intent)
    }

    open fun startWebAty(context: Context, intent: Intent) {
        intent.setClass(context, WebViewAty::class.java)
        context.startActivity(intent)
    }
}