package com.konsung.cla.demo2.presenter

import android.content.Context
import com.konsung.basic.bean.site.SiteCollectBean
import com.konsung.basic.config.RequestData
import com.konsung.basic.model.BaseModel
import com.konsung.basic.model.Model
import com.konsung.basic.presenter.BasePresenter1
import com.konsung.basic.presenter.Presenter
import com.konsung.basic.presenter.UiView
import com.konsung.basic.util.toast
import com.konsung.cla.demo2.R

/**
 * 添加网站presenter实现类
 */
class AddSitePresenterImpl(uiView: AddSiteView?) : BasePresenter1<SiteCollectBean, AddSiteView, AddSiteModel>(uiView, AddSiteModelImpl()), AddSitePresenter {

    companion object {
        val TAG: String = AddSitePresenterImpl::class.java.simpleName
    }

    enum class Action {
        ADD, EDIT, DELETE
    }

    private var action = Action.ADD

    override fun success(t: SiteCollectBean, refreshData: Boolean) {
        when (action) {
            Action.ADD -> getUiView()?.addSiteSuccess(t)
            Action.EDIT -> getUiView()?.editSiteSuccess(t)
            Action.DELETE -> getUiView()?.deleteSiteSuccess(t)
        }
    }

    override fun failed(message: String, refreshData: Boolean) {
        when (action) {
            Action.ADD -> getUiView()?.addSiteFailed(message)
            Action.EDIT -> getUiView()?.editSiteFailed(message)
            Action.DELETE -> getUiView()?.deleteSiteFailed(message)
        }
    }

    override fun addSite(name: String, link: String) {
        action = Action.ADD
        request { context, _, requestData -> model.addSite(context, name, link, requestData) }
    }

    override fun deleteSite(id: Int) {
        action = Action.DELETE
    }

    override fun editSite(id: Int, name: String, link: String) {

        if (id < 0) {
            getContext()?.toast(TAG, R.string.id_is_error)
            return
        }

        action = Action.EDIT
        request { context, _, requestData -> model.editSite(context, id, name, link, requestData) }
    }
}

/**
 * 添加网站model实现类
 */
class AddSiteModelImpl : BaseModel<SiteCollectBean>(), AddSiteModel {

    override fun addSite(context: Context?, name: String, link: String, result: RequestData<SiteCollectBean>) {
        request(context, result) { ctx, data ->
            httpHelper.addSite(ctx, name, link, data)
        }
    }

    override fun deleteSite(context: Context?, id: Int, result: RequestData<SiteCollectBean>) {

    }

    override fun editSite(context: Context?, id: Int, name: String, link: String, result: RequestData<SiteCollectBean>) {
        request(context, result) { ctx, data ->
            httpHelper.editSite(ctx, id, name, link, data)
        }
    }
}

/**
 * 添加网站model
 */
interface AddSiteModel : Model {
    /**
     * 添加网站
     */
    fun addSite(context: Context?, name: String, link: String, result: RequestData<SiteCollectBean>)

    /**
     * 删除网站
     */
    fun deleteSite(context: Context?, id: Int, result: RequestData<SiteCollectBean>)

    /**
     * 编辑网站
     */
    fun editSite(context: Context?, id: Int, name: String, link: String, result: RequestData<SiteCollectBean>)
}

/**
 * 添加网站presenter
 */
interface AddSitePresenter : Presenter {
    /**
     * 添加网站
     */
    fun addSite(name: String, link: String)

    /**
     * 删除网站
     */
    fun deleteSite(id: Int)

    /**
     * 编辑网站
     */
    fun editSite(id: Int, name: String, link: String)
}

/**
 * 添加网站view
 */
interface AddSiteView : UiView {

    /**
     * 添加网站
     */
    fun addSiteSuccess(bean: SiteCollectBean)

    /**
     * 添加失败
     */
    fun addSiteFailed(error: String)

    /**
     * 删除网站
     */
    fun deleteSiteSuccess(bean: SiteCollectBean)

    /**
     * 删除失败
     */
    fun deleteSiteFailed(error: String)

    /**
     * 编辑网站
     */
    fun editSiteSuccess(bean: SiteCollectBean)

    /**
     * 编辑失败
     */
    fun editSiteFailed(error: String)
}