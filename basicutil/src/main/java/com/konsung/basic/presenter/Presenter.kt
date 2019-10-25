package com.konsung.basic.presenter

import android.content.Context
import com.konsung.basic.config.RequestData
import com.konsung.basic.model.Model
import java.lang.ref.WeakReference

interface Presenter {
    fun destroy()
}

abstract class BasePresenter<V : UiView, M : Model>(private var uiView: V?) : Presenter {

    val model: M by lazy { initModel() }

    private val ctxReference: WeakReference<V?> = WeakReference(uiView)

    fun getContext(): Context? = ctxReference.get()?.getUiContext()

    fun getUiView(): V? = ctxReference.get()

    override fun destroy() {
        uiView = null
    }

    abstract fun initModel(): M
}

abstract class BasePresenter1<T, V : UiView, M : Model>(uiView: V?) : BasePresenter<V, M>(uiView) {

    var pageStart = 0
    var page = pageStart

    /**
     * result放在这里是为了每次请求之前都要先停止之前的请求
     */
    var result: RequestData<T>? = null

    /**
     * 接口调用成功，可重写此方法处理返回的数据
     */
    open fun success(t: T, refreshData: Boolean) {

    }

    /**
     * 接口调用成功
     */
    open fun success(refreshData: Boolean) {

    }

    /**
     * 接口调用失败
     */
    open fun failed(message: String, refreshData: Boolean) {


    }

    /**
     * 当前没有网络
     */
    open fun noNetwork(refreshData: Boolean) {

    }

    open fun empty(refreshData: Boolean) {
        if (refreshData) {
            getUiView()?.showEmptyView()
        }
    }

    /**
     * 方法调用完毕，已经产生结果
     */
    open fun complete(success: Boolean, refreshData: Boolean) {

    }

    inline fun request(refreshData: Boolean = true, page: Int = 0, request: (Context, Int, RequestData<T>) -> Unit) {

        val ctx = getContext() ?: return
        stop()
        result = setRequestResult(refreshData)
        request.invoke(ctx, page, result!!)
    }

    /**
     * 接口调用返回结果
     */
    open fun setRequestResult(refresh: Boolean): RequestData<T> {

        val presenter = this
        stop()

        result = object : RequestData<T>() {

            override fun success(t: T) {
                val context = getContext()
                if (context == null) {
                    destroy()
                    return
                }

                if (stop) {
                    return
                }

                getUiView()?.showContentView()
                presenter.success(t, refreshData)
            }

            override fun success() {
                val context = getContext()
                if (context == null) {
                    destroy()
                    return
                }

                if (stop) {
                    return
                }

                getUiView()?.showContentView()
                presenter.success(refreshData)
            }

            override fun failed(message: String) {
                val context = getContext()
                if (context == null) {
                    destroy()
                    return
                }


                if (stop) {
                    return
                }

                //如果之前已经成功获取数据，并且这一次是刷新全部数据的话，那么就去显示错误视图
                //否则只需要提示错误信息就可以了
                if (refreshData) {
                    getUiView()?.showErrorView()
                }

                if (page > pageStart) {
                    --page
                }

                presenter.failed(message, refreshData)
            }

            override fun complete(success: Boolean) {
                val context = getContext()
                if (context == null) {
                    destroy()
                    return
                }

                if (stop) {
                    return
                }

                //如果之前已经成功获取数据，并且这一次是刷新全部数据的话，那么就去显示没有网络视图
                //否则只需要提示错误信息就可以了
                if (refreshData) {
                    getUiView()?.showNoNetworkView()
                }

                presenter.complete(success, refreshData)
            }

            override fun noNetwork() {
                val context = getContext()
                if (context == null) {
                    destroy()
                    return
                }

                if (stop) {
                    return
                }

                getUiView()?.loadComplete(refreshData)
                presenter.noNetwork(refreshData)
            }
        }

        result!!.refreshData = refresh
        return result!!
    }


    /**
     * 停止上一次的请求
     */
    open fun stop() {
        result?.stop = true
    }

    /**
     * activity销毁时调用这个方法
     */
    override fun destroy() {
        stop()
        super.destroy()
    }
}

abstract class RefreshPresenter<T, V : UiView, M : Model>(uiView: V?) : BasePresenter1<T, V, M>(uiView) {

    inline fun refresh(request3: (Context, Int, RequestData<T>) -> Unit) {
        page = pageStart
        request(true, page, request3)
    }

    inline fun loadMore(request3: (Context, Int, RequestData<T>) -> Unit) {
        page++
        request(false, page, request3)
    }

    abstract fun refresh()
    abstract fun loadMore()
}