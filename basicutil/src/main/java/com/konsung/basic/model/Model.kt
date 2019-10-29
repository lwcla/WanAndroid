package com.konsung.basic.model

import android.content.Context
import com.konsung.basic.config.RequestData
import com.konsung.basic.net.HttpHelper
import com.konsung.basic.net.HttpHelperImpl

interface Model

abstract class BaseModel<T> {

    protected val httpHelper: HttpHelper by lazy { HttpHelperImpl.create() }

    /**
     * requestData放在这里是为了每次请求之前都要先停止之前的请求
     */
    var requestData: RequestData<T>? = null
    /**
     * presenter传过来的对象
     */
    var result: RequestData<T>? = null

    /**
     * 是否弹出toast
     */
    var toast = true

    /**
     * 接口调用成功，可重写此方法处理返回的数据
     */
    open fun success(t: T) {
        result?.success(t)
    }

    /**
     * 接口调用成功
     */
    open fun success() {
        result?.success()
    }

    /**
     * 接口调用失败
     */
    open fun failed(message: String) {
        result?.failed(message)
    }

    /**
     * 当前没有网络
     */
    open fun noNetwork() {
        result?.noNetwork()
    }

    /**
     * 方法调用完毕，已经产生结果
     */
    open fun complete(success: Boolean) {
        result?.complete(success)
    }

    /**
     * 请求
     * @param context context
     * @param result 请求结果回调
     * @param toast 是否弹出错误提示
     * @param request 请求
     *
     */
    inline fun request(context: Context?, result: RequestData<T>, toast: Boolean = true, request: (Context, RequestData<T>) -> Unit) {

        val ctx = context ?: return

        this.result = result
        stop()

        requestData = setRequestResult(toast)
        request.invoke(ctx, requestData!!)
    }

    /**
     * 接口调用返回结果
     */
    open fun setRequestResult(toast: Boolean): RequestData<T> {

        val presenter = this
        requestData?.stop = true

        requestData = object : RequestData<T>() {

            override fun success(t: T) {

                if (stop) {
                    return
                }

                presenter.success(t)
            }

            override fun success() {

                if (stop) {
                    return
                }

                presenter.success()
            }

            override fun failed(message: String) {

                if (stop) {
                    return
                }

                presenter.failed(message)
            }

            override fun complete(success: Boolean) {

                if (stop) {
                    return
                }

                presenter.complete(success)
            }

            override fun noNetwork() {

                if (stop) {
                    return
                }

                presenter.noNetwork()
            }
        }

        requestData!!.toast = toast

        return requestData!!
    }

    /**
     * 停止上一次的请求
     */
    open fun stop() {
        requestData?.stop = true
    }
}