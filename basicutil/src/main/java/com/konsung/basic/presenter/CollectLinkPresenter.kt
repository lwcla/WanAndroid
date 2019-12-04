package com.konsung.basic.presenter

import android.content.Context
import com.konsung.basic.bean.HomeData
import com.konsung.basic.config.BaseConfig
import com.konsung.basic.config.RequestData
import com.konsung.basic.model.BaseModel
import com.konsung.basic.model.Model
import com.konsung.basic.util.AppUtils
import com.konsung.basic.util.R
import com.konsung.basic.util.toast

/***
 * 收藏链接Presenter实现类
 */
class CollectLinkPresenterImpl(uiView: CollectLinkView?) : BasePresenter1<HomeData.DatasBean, CollectLinkView, CollectLinkModel>(uiView, CollectLinkModelImpl()), CollectLinkPresenter {

    override fun success(t: HomeData.DatasBean, refreshData: Boolean) {
        //需要吧收藏状态置为true，出现过后台返回的收藏数据 收藏状态为false的情况
        t.collect = true
        getUiView()?.collectLinkSuccess(t)
    }

    override fun complete(success: Boolean, refreshData: Boolean) {

        val ctx = getContext() ?: return

        //直接在这里弹出提示
        if (success) {
            ctx.toast(BaseConfig.TAG, R.string.collect_success)
        } else {
            ctx.toast(BaseConfig.TAG, R.string.collect_failed)
        }
    }

    override fun failed(message: String, refreshData: Boolean) {
        getUiView()?.collectLinkFailed(message)
    }

    override fun collectLink(title: String, author: String, link: String) {
        val ctx = getContext() ?: return

        if (!AppUtils.instance.hasLogined(ctx)) {
            //还没有登录，打开登录界面
            ctx.toast(BaseConfig.TAG, R.string.collect_after_login)
            AppUtils.startLoginAty(ctx)
            return
        }

        request { c, _, result -> model.collectLink(c, title, author, link, result) }
        return
    }
}

/**
 * 收藏链接Model实现类
 */
class CollectLinkModelImpl : BaseModel<HomeData.DatasBean>(), CollectLinkModel {

    override fun collectLink(context: Context?, title: String, author: String, link: String, result: RequestData<HomeData.DatasBean>) {
        request(context, result) { ctx, data ->
            httpHelper.collectLink(ctx, title, author, link, data)
        }
    }
}

/**
 * 收藏链接Model
 */
interface CollectLinkModel : Model {
    fun collectLink(context: Context?, title: String, author: String, link: String, result: RequestData<HomeData.DatasBean>)
}

/**
 * 收藏链接Presenter
 */
interface CollectLinkPresenter : Presenter {
    fun collectLink(title: String, author: String, link: String)
}

/**
 * 收藏连接view
 */
interface CollectLinkView : UiView {

    fun collectLinkSuccess(t: HomeData.DatasBean)

    fun collectLinkFailed(error: String)
}

