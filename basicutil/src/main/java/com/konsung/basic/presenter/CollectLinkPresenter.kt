package com.konsung.basic.presenter

import android.content.Context
import com.konsung.basic.bean.HomeData
import com.konsung.basic.config.BaseConfig
import com.konsung.basic.util.AppUtils
import com.konsung.basic.util.R
import com.konsung.basic.util.toast

class CollectLinkPresenter(uiView: UiView?, view: CollectLinkView?) : BasicPresenter2<HomeData.DatasBean, CollectLinkView>(uiView, view) {

    override fun success(context: Context, t: HomeData.DatasBean) {
        t.collect = true
        super.success(context, t)
    }

    override fun complete(context: Context, success: Boolean) {
        if (success) {
            context.toast(BaseConfig.TAG, R.string.collect_success)
        } else {
            context.toast(BaseConfig.TAG, R.string.collect_failed)
        }
        super.complete(context, success)
    }

    fun collectLink(title: String, author: String, link: String): Boolean {

        val ctx = getContext() ?: return false
        if (!AppUtils.instance.hasLogined(ctx)) {
            //还没有登录，打开登录界面
            ctx.toast(BaseConfig.TAG, R.string.collect_after_login)
            AppUtils.startLoginAty(ctx)
            return false
        }

        request { c, result -> httpHelper.collectLink(c, title, author, link, result) }
        return true
    }
}

open class CollectLinkView : BasicView<HomeData.DatasBean>()