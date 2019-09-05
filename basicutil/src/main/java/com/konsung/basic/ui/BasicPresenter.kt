package com.konsung.basic.ui

import android.content.Context
import com.konsung.basic.config.RequestResult
import com.konsung.basic.net.HttpHelper
import com.konsung.basic.net.HttpHelperImpl

abstract class BasicPresenter {
    protected val httpHelper: HttpHelper by lazy { HttpHelperImpl.create() }

    abstract fun destroy()
}

abstract class BasePresenter<T, V : BasicView<T>>(var view: V?) : BasicPresenter() {

    var result: RequestResult<T>? = null

    /**
     * 停止上一次的请求
     */
    fun stop() {
        result?.stop = true
    }


    inline fun request(context: Context?, request: (Context, RequestResult<T>) -> Unit) {

        val ctx = context ?: return

        stop()
        result = setRequestResult()
        request.invoke(ctx, result!!)
    }

    open fun setRequestResult(): RequestResult<T> {

        val presenter = this

        return object : RequestResult<T>(view) {

            override fun success(t: T) {
                presenter.success(t)
            }

            override fun success() {
                presenter.success()
            }

            override fun failed(message: String) {
                presenter.failed(message)
            }

            override fun complete() {
                presenter.complete()
            }
        }
    }

    open fun success(t: T) {
        view?.success(t)
    }

    open fun success() {
        view?.success()
    }

    open fun failed(message: String) {
        view?.failed(message)
    }

    open fun noNetwork() {
        view?.noNetwork()
    }

    open fun complete() {
        view?.complete()
    }

    override fun destroy() {
        stop()
        view = null
    }
}

abstract class BasicView<T> {

    open fun success(t: T) {

    }

    open fun success() {

    }

    open fun failed(string: String) {

    }

    open fun noNetwork() {

    }

    open fun complete() {

    }
}

