package com.konsung.cla.demo2.presenter

import android.content.Context
import com.konsung.basic.bean.search.SearchKey
import com.konsung.basic.config.RequestData
import com.konsung.basic.model.BaseModel
import com.konsung.basic.model.Model
import com.konsung.basic.presenter.BasePresenter1
import com.konsung.basic.presenter.Presenter
import com.konsung.basic.presenter.UiView

/**
 * 加载搜索热词Presenter实现类
 */
class LoadSearchHotPresenterImpl(uiView: LoadSearchHotView?) : BasePresenter1<List<SearchKey>, LoadSearchHotView, LoadSearchHotModel>(uiView, LoadSearchHotModelImpl()), LoadSearchHotPresenter {

    override fun success(t: List<SearchKey>, refreshData: Boolean) {

        //过滤无用数据
        val list = mutableListOf<SearchKey>()
        for (key in t) {

            if (key.name.isNullOrEmpty()) {
                continue
            }

            list.add(key)
        }

        getUiView()?.loadSearchHotSuccess(list)
    }

    override fun failed(message: String, refreshData: Boolean) {
        getUiView()?.loadSearchHotFailed(message)
    }

    /**
     * 加载搜索热词
     */
    override fun loadHotKey() {
        request { context, _, requestData -> model.loadHotKey(context, requestData) }
    }
}

/**
 * 加载搜索热词model实现类
 */
private class LoadSearchHotModelImpl : BaseModel<List<SearchKey>>(), LoadSearchHotModel {

    override fun loadHotKey(context: Context?, result: RequestData<List<SearchKey>>) {
        request(context, result) { ctx, requestData ->
            httpHelper.fetchSearchHotKey(ctx, requestData)
        }
    }
}

/**
 * 加载搜索热词Presenter
 */
interface LoadSearchHotPresenter : Presenter {
    fun loadHotKey()
}

/**
 * 加载搜索热词model
 */
interface LoadSearchHotModel : Model {
    fun loadHotKey(context: Context?, result: RequestData<List<SearchKey>>)
}

/**
 * 加载搜索热词view
 */
interface LoadSearchHotView : UiView {

    fun loadSearchHotSuccess(list: List<SearchKey>)

    fun loadSearchHotFailed(error: String)
}
