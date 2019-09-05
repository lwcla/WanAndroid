package com.konsung.cla.demo2.util

import android.content.Context
import android.content.Intent
import com.konsung.cla.demo2.config.Config
import com.konsung.cla.demo2.login.LoginAty
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

    /**
     * 启动webActivity
     * @param context context
     * @param title 标题
     * @param link url
     * @param collect 是否已经收藏
     */
    open fun startWebAty(context: Context, title: String?, link: String?, artId: Int, collect: Boolean) {
        val intent = Intent()
        intent.putExtra(Config.WEB_TITLE, title)
        intent.putExtra(Config.WEB_URL, link)
        intent.putExtra(Config.WEB_ARTICLE_ID, artId)
        intent.putExtra(Config.IS_COLLECT, collect)
        startWebAty(context, intent)
    }

    open fun startLoginAty(context: Context?) {
        context?.let {
            val intent = Intent()
            intent.setClass(it, LoginAty::class.java)
            it.startActivity(intent)
        }
    }
}