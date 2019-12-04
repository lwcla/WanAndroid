package com.cla.navigation

import android.content.Context
import com.konsung.basic.bean.navigation.NavigationBean
import com.konsung.basic.config.RequestData
import com.konsung.basic.model.BaseModel
import com.konsung.basic.model.Model
import com.konsung.basic.presenter.BasePresenter1
import com.konsung.basic.presenter.Presenter
import com.konsung.basic.presenter.UiView

/*class loadNavigation(uiView: UiView?, view: LoadNavigationView?) : BasicPresenter2<List<NavigationBean>, LoadNavigationView>(uiView, view) {

    override fun success(context: Context, t: List<NavigationBean>) {

    }

    fun load() {
        request { ctx, result -> httpHelper.fetNavigationList(ctx, result) }
    }

}

open class LoadNavigationView : BasicView<List<NavigationBean>>()*/

/**
 * 加载导航数据Presenter实现类
 */
class LoadNavigationPresenterImpl(uiView: LoadNavigationView?) : BasePresenter1<List<NavigationBean>, LoadNavigationView, LoadNavigationModel>(uiView, LoadNavigationModelImpl()), LoadNavigationPresenter {

    override fun success(t: List<NavigationBean>, refreshData: Boolean) {

        //过滤无用数据
        val list = mutableListOf<NavigationBean>()
        list.addAll(t.filter {
            !it.name.isNullOrEmpty() && it.articles?.size ?: 0 > 0
        })

        getUiView()?.loadNavigationSuccess(list)
    }

    override fun failed(message: String, refreshData: Boolean) {
        getUiView()?.loadNavigationFailed(message)
    }

    override fun loadNavigation() {
        request { context, _, requestData -> model.loadNavigation(context, requestData) }
    }
}

/**
 * 加载导航数据model实现类
 */
private class LoadNavigationModelImpl : BaseModel<List<NavigationBean>>(), LoadNavigationModel {

    override fun loadNavigation(context: Context?, result: RequestData<List<NavigationBean>>) {
        request(context, result) { ctx, requestData ->
            httpHelper.fetNavigationList(ctx, requestData)
        }
    }
}

/**
 * 加载导航数据Presenter
 */
interface LoadNavigationPresenter : Presenter {
    fun loadNavigation()
}

/**
 * 加载导航数据model
 */
interface LoadNavigationModel : Model {
    fun loadNavigation(context: Context?, result: RequestData<List<NavigationBean>>)
}

/**
 * 加载导航数据view
 */
interface LoadNavigationView : UiView {
    fun loadNavigationSuccess(list: List<NavigationBean>)

    fun loadNavigationFailed(error: String)
}