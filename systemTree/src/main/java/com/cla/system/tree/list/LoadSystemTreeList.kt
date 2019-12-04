package com.cla.system.tree.list

import android.content.Context
import com.konsung.basic.bean.tree.SystemTreeListBean
import com.konsung.basic.config.RequestData
import com.konsung.basic.model.BaseModel
import com.konsung.basic.model.Model
import com.konsung.basic.presenter.BasePresenter1
import com.konsung.basic.presenter.Presenter
import com.konsung.basic.presenter.UiView

/**
 * 加载体系数据Presenter实现类
 */
class LoadSystemTreeListPresenterImpl(uiView: LoadSystemTreeListView?) :
        BasePresenter1<List<SystemTreeListBean>, LoadSystemTreeListView, LoadSystemTreeListModel>(uiView, LoadSystemTreeListModelImpl()), LoadSystemTreeListPresenter {

    override fun success(t: List<SystemTreeListBean>, refreshData: Boolean) {
        getUiView()?.loadSystemTreeListSuccess(t)
    }

    override fun failed(message: String, refreshData: Boolean) {
        getUiView()?.loadSystemTreeListFailed(message)
    }

    /**
     * 加载体系数据
     */
    override fun loadSystemTreeList() {
        request { context, _, requestData -> model.loadSystemTreeList(context, requestData) }
    }
}

/**
 * 加载体系数据model实现类
 */
private class LoadSystemTreeListModelImpl : BaseModel<List<SystemTreeListBean>>(), LoadSystemTreeListModel {

    override fun loadSystemTreeList(context: Context?, result: RequestData<List<SystemTreeListBean>>) {

        request(context, result) { ctx, requestData ->
            httpHelper.fetchTreeList(ctx, requestData)
        }
    }
}

/**
 * 加载体系数据Presenter
 */
interface LoadSystemTreeListPresenter : Presenter {
    fun loadSystemTreeList()
}

/**
 * 加载体系数据model
 */
interface LoadSystemTreeListModel : Model {
    fun loadSystemTreeList(context: Context?, result: RequestData<List<SystemTreeListBean>>)
}

/**
 * 加载体系数据view
 */
interface LoadSystemTreeListView : UiView {

    fun loadSystemTreeListSuccess(list: List<SystemTreeListBean>)

    fun loadSystemTreeListFailed(error: String)
}

