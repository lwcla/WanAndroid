package com.konsung.cla.demo2.presenter

import android.content.Context
import com.konsung.basic.bean.site.SiteCollectBean
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

    companion object {
        val TAG: String = AddSiteCollectPresenter::class.java.simpleName
    }

    enum class Action {
        ADD, EDIT, DELETE
    }

    private var action = Action.ADD

    override fun complete(context: Context, success: Boolean) {

        val toastRes: Int = if (success) {
            when (action) {
                Action.ADD -> R.string.collect_success
                Action.EDIT -> R.string.edit_success
                Action.DELETE -> R.string.delete_success
            }
        } else {

            when (action) {
                Action.ADD -> R.string.collect_failed
                Action.EDIT -> R.string.edit_failed
                Action.DELETE -> R.string.delete_failed
            }
        }

        getContext()?.toast(TAG, toastRes)
        super.complete(context, success)
    }

    fun addSite(name: String, link: String) {
        action = Action.ADD
        request { ctx, result -> httpHelper.addSite(ctx, name, link, result) }
    }

    fun editSite(id: Int, name: String, link: String) {

        if (id < 0) {
            getContext()?.toast(TAG, R.string.id_is_error)
            return
        }

        action = Action.EDIT
        request { ctx, result -> httpHelper.editSite(ctx, id, name, link, result) }
    }
}

open class AddSiteCollectView : BasicView<SiteCollectBean>()
