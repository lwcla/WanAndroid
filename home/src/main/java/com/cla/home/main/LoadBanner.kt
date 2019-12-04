package com.cla.home.main

import android.content.Context
import com.konsung.basic.bean.BannerData
import com.konsung.basic.config.RequestData
import com.konsung.basic.model.BaseModel
import com.konsung.basic.model.Model
import com.konsung.basic.presenter.BasePresenter1
import com.konsung.basic.presenter.Presenter
import com.konsung.basic.presenter.UiView

/**
 * 加载banner数据Presenter实现类
 */
class LoadBannerPresenterImpl(uiView: LoadBannerView?) : BasePresenter1<List<BannerData>, LoadBannerView, LoadBannerModel>(uiView, LoadBannerModelImpl()), LoadBannerPresenter {

    override fun success(t: List<BannerData>, refreshData: Boolean) {
        getUiView()?.loadBannerSuccess(t)
    }

    override fun failed(message: String, refreshData: Boolean) {
        getUiView()?.loadBannerFailed(message)
    }

    override fun loadBanner() {
        request { ctx, _, result ->
            model.loadBanner(ctx, result)
        }
    }
}

/**
 * 加载banner数据Model实现类
 */
private class LoadBannerModelImpl : BaseModel<List<BannerData>>(), LoadBannerModel {

    override fun loadBanner(context: Context?, result: RequestData<List<BannerData>>) {
        request(context, result) { ctx, requestData ->
            httpHelper.loadBanner(ctx, requestData)
        }
    }
}

/**
 * 加载banner数据Model
 */
interface LoadBannerModel : Model {
    fun loadBanner(context: Context?, result: RequestData<List<BannerData>>)
}

/**
 * 加载banner数据Presenter
 */
interface LoadBannerPresenter : Presenter {
    fun loadBanner()
}

/**
 * 加载banner数据view
 */
interface LoadBannerView : UiView {
    fun loadBannerSuccess(t: List<BannerData>)

    fun loadBannerFailed(error: String)
}