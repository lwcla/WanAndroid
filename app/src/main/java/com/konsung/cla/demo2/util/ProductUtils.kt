package com.konsung.cla.demo2.util

import android.content.Context
import android.content.Intent
import com.konsung.basic.config.BaseConfig
import com.konsung.cla.demo2.login.LoginAty
import com.konsung.cla.demo2.main.MainActivity
import com.konsung.cla.demo2.web.WebViewAty

open class ProductUtils {

    open fun startMainAty(context: Context) {
        val intent = Intent();
        intent.setClass(context, MainActivity::class.java)
        context.startActivity(intent)
    }

    open fun startWebAty(context: Context?, intent: Intent) {
        if (context == null) {
            return
        }

        intent.setClass(context, WebViewAty::class.java)
        context.startActivity(intent)
    }

    /**
     * 启动webActivity
     * @param context context
     * @param title 标题
     * @param link url
     * @param artId 文章id
     * @param dataPosition 数据在列表中的位置
     * @param collect 是否已经收藏
     * @param needCollect 是否需要收藏按钮
     */
    open fun startWebAty(context: Context?, title: String?, link: String?, artId: Int, collect: Boolean, dataPosition: Int = -1, needCollect: Boolean = true) {
        val intent = Intent()
        intent.putExtra(BaseConfig.WEB_TITLE, title)
        intent.putExtra(BaseConfig.WEB_URL, link)
        intent.putExtra(BaseConfig.WEB_ARTICLE_ID, artId)
        intent.putExtra(BaseConfig.WEB_DATA_POSITION, dataPosition)
        intent.putExtra(BaseConfig.IS_COLLECT, collect)
        intent.putExtra(BaseConfig.NEED_COLLECT, needCollect)
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