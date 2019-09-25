package com.konsung.basic.ui

import android.content.Context
import com.konsung.basic.config.RequestResult
import com.konsung.basic.net.HttpHelper
import com.konsung.basic.net.HttpHelperImpl
import java.lang.ref.WeakReference

interface UiView {

    fun getUiContext(): Context?

    fun showContentView()

    fun showErrorView()

    fun showNoNetworkView()

    fun showLoadView()
}

abstract class BasicPresenter(uiView: UiView?) {

    private val ctxReference: WeakReference<UiView?> = WeakReference(uiView)

    protected val httpHelper: HttpHelper by lazy { HttpHelperImpl.create() }

    var refreshData = true

    fun getContext(): Context? = ctxReference.get()?.getUiContext()

    fun getUiView(): UiView? = ctxReference.get()

    abstract fun destroy()
}

abstract class BasePresenter1<T, V : BasicView<T>>(uiView: UiView?, var view: V?) : BasicPresenter(uiView) {

    /**
     * activity销毁时调用这个方法
     */
    override fun destroy() {
        view = null
    }

}

abstract class BasePresenter2<T, V : BasicView<T>>(uiView: UiView?, view: V?) : BasePresenter1<T, V>(uiView, view) {

    /**
     * result放在这里是为了每次请求之前都要先停止之前的请求
     */
    var result: RequestResult<T>? = null
    private var success = false

    /**
     * 接口调用成功，可重写此方法处理返回的数据
     */
    open fun success(context: Context, t: T) {
        getUiView()?.showContentView()
        success = true
        view?.success(t, refreshData)
    }

    /**
     * 接口调用成功
     */
    open fun success(context: Context) {
        getUiView()?.showContentView()
        success = true
        view?.success(refreshData)
    }

    /**
     * 接口调用失败
     */
    open fun failed(context: Context, message: String) {
        success = false

        //如果之前已经成功获取数据，并且这一次是刷新全部数据的话，那么就去显示错误视图
        //否则只需要提示错误信息就可以了
        if (refreshData) {
            getUiView()?.showErrorView()
        }

        view?.failed(message)
    }

    /**
     * 当前没有网络
     */
    open fun noNetwork(context: Context) {
        success = false

        //如果之前已经成功获取数据，并且这一次是刷新全部数据的话，那么就去显示没有网络视图
        //否则只需要提示错误信息就可以了
        if (refreshData) {
            getUiView()?.showNoNetworkView()
        }
        view?.noNetwork()
    }

    /**
     * 方法调用完毕，已经产生结果
     */
    open fun complete(context: Context) {
        view?.complete()
    }

    inline fun request(request: (Context, RequestResult<T>) -> Unit) {

        val ctx = getContext() ?: return

        stop()

        result = setRequestResult()
        result?.refreshData = refreshData
        request.invoke(ctx, result!!)
    }

    /**
     * 接口调用返回结果
     */
    open fun setRequestResult(): RequestResult<T> {

        val presenter = this

        return object : RequestResult<T>(view) {

            override fun success(t: T) {
                val context = getContext()
                if (context == null) {
                    stop()
                    return
                }

                presenter.success(context, t)
            }

            override fun success() {
                val context = getContext()
                if (context == null) {
                    destroy()
                    return
                }

                presenter.success(context)
            }

            override fun failed(message: String) {
                val context = getContext()
                if (context == null) {
                    destroy()
                    return
                }

                presenter.failed(context, message)
            }

            override fun complete() {
                val context = getContext()
                if (context == null) {
                    destroy()
                    return
                }

                presenter.complete(context)
            }

            override fun noNetwork() {
                val context = getContext()
                if (context == null) {
                    destroy()
                    return
                }

                presenter.noNetwork(context)
            }
        }
    }

    /**
     * activity销毁时调用这个方法
     */
    override fun destroy() {
        stop()
        view = null
    }

    /**
     * 停止上一次的请求
     */
    open fun stop() {
        result?.stop = true
    }
}

abstract class BasePresenter3<T, V : BasicView<T>>(uiView: UiView?, view: V?) : BasePresenter2<T, V>(uiView, view) {

    var pageStart = 0
    var page = pageStart

    override fun failed(context: Context, message: String) {

        if (page > pageStart) {
            --page
        }

        super.failed(context, message)
    }

    inline fun refresh(request3: (Context, RequestResult<T>) -> Unit) {
        refreshData = true
        page = pageStart
        request(request3)
    }

    inline fun loadMore(request3: (Context, RequestResult<T>) -> Unit) {
        refreshData = false
        page++
        request(request3)
    }

    abstract fun refresh()

    abstract fun loadMore()
}

abstract class BasicView<T> {

    open fun success(t: T, refreshData: Boolean) {

    }

    open fun success(refreshData: Boolean) {

    }

    open fun failed(string: String) {

    }

    open fun noNetwork() {

    }

    open fun complete() {

    }
}

