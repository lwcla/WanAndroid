package com.konsung.basic.config

import com.konsung.basic.ui.BasicView

abstract class RequestResult<T>(private val view: BasicView<T>?) {

    /**
     * 是否在拿到请求结果时就弹出toast提示
     */
    var toast = true

    var stop = false

    var position = -1

    /**
     * 这次的操作是去收藏还是去取消收藏
     */
    var toCollect = false
    /**
     * 收藏文章的id
     */
    var collectId = -1

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
}