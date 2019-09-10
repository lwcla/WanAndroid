package com.konsung.cla.demo2.util

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.view.View
import com.konsung.basic.bean.ScreenImageData
import com.konsung.basic.config.BaseConfig
import com.konsung.basic.ui.ScreenImageAty
import com.konsung.cla.demo2.login.LoginAty
import com.konsung.cla.demo2.main.MainActivity
import com.konsung.cla.demo2.web.WebViewAty

open class ProductUtils {

    open fun startMainAty(context: Context) {
        val intent = Intent()
        intent.setClass(context, MainActivity::class.java)
        context.startActivity(intent)
    }

    open fun startScreenImageAty(context: Context, data: ScreenImageData) {
        val intent = Intent()
        intent.putExtra(BaseConfig.SCREEN_IAMGE_DATA, data)
        intent.setClass(context, ScreenImageAty::class.java)
        context.startActivity(intent)
    }

    open fun startScreenImageAty(context: Context, url: String) {
        val data = ScreenImageData(listOf(url), 0)
        startScreenImageAty(context, data)
    }

    open fun startScreenImageAty(context: Context, urls: List<String>, currentIndex: Int = 0) {
        val data = ScreenImageData(urls, currentIndex)
        startScreenImageAty(context, data)
    }

    open fun setWebAty(context: Context, intent: Intent) {
        intent.setClass(context, WebViewAty::class.java)
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
        if (context == null) {
            return
        }

        val intent = Intent()
        intent.putExtra(BaseConfig.WEB_TITLE, title)
        intent.putExtra(BaseConfig.WEB_URL, link)
        intent.putExtra(BaseConfig.WEB_ARTICLE_ID, artId)
        intent.putExtra(BaseConfig.WEB_DATA_POSITION, dataPosition)
        intent.putExtra(BaseConfig.IS_COLLECT, collect)
        intent.putExtra(BaseConfig.NEED_COLLECT, needCollect)
        setWebAty(context, intent)
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
    open fun startWebAty(activity: Activity?, context: Context?, view: View, title: String?, link: String?, artId: Int, collect: Boolean, dataPosition: Int = -1, needCollect: Boolean = true) {

        if (context == null || activity == null) {
            return
        }

        val intent = Intent()
        intent.putExtra(BaseConfig.WEB_TITLE, title)
        intent.putExtra(BaseConfig.WEB_URL, link)
        intent.putExtra(BaseConfig.WEB_ARTICLE_ID, artId)
        intent.putExtra(BaseConfig.WEB_DATA_POSITION, dataPosition)
        intent.putExtra(BaseConfig.IS_COLLECT, collect)
        intent.putExtra(BaseConfig.NEED_COLLECT, needCollect)

        setWebAty(context, intent)
        activity.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity, view, "tvTitle").toBundle())
    }

    open fun startLoginAty(context: Context?) {
        context?.let {
            val intent = Intent()
            intent.setClass(it, LoginAty::class.java)
            it.startActivity(intent)
        }
    }
}