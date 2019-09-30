package com.konsung.cla.demo2.presenter

import android.content.Context
import com.konsung.basic.bean.site.SiteCollectBean
import com.konsung.basic.config.BaseConfig
import com.konsung.basic.ui.BasePresenter2
import com.konsung.basic.ui.BasicView
import com.konsung.basic.ui.UiView
import com.konsung.basic.util.R
import com.konsung.basic.util.toast

/**
 * 网站收藏
 */
class SiteCollectPresenter(uiView: UiView?, view: SiteCollectView?) : BasePresenter2<List<SiteCollectBean>, SiteCollectView>(uiView, view) {

    override fun success(context: Context, t: List<SiteCollectBean>) {
        val list = t.reversed()
        super.success(context, list)
    }

    fun refresh() {
        request { ctx, result -> httpHelper.fetchCollectSiteList(ctx, result) }
    }
}

open class SiteCollectView : BasicView<List<SiteCollectBean>>()


class AddSiteCollectPresenter(uiView: UiView?, view: AddSiteCollectView?) : BasePresenter2<SiteCollectBean, AddSiteCollectView>(uiView, view) {

    override fun complete(context: Context, success: Boolean) {
        if (success) {
            context.toast(BaseConfig.TAG, R.string.collect_success)
        } else {
            context.toast(BaseConfig.TAG, R.string.collect_failed)
        }
        super.complete(context, success)
    }

    fun addSite(name: String, link: String) {
        request { ctx, result -> httpHelper.addSite(ctx, name, link, result) }
    }
}

open class AddSiteCollectView : BasicView<SiteCollectBean>()
