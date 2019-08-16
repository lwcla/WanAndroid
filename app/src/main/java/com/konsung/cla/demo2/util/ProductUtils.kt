package com.konsung.cla.demo2.util

import android.content.Context
import android.content.Intent
import com.konsung.cla.demo2.main.MainActivity

open class ProductUtils {

    open fun startMainAty(context: Context) {
        val intent = Intent();
        intent.setClass(context, MainActivity::class.java)
        context.startActivity(intent)
    }

}