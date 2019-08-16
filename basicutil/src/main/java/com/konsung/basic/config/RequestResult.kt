package com.konsung.basic.config

abstract class RequestResult<T> {

    /**
     * 是否在拿到请求结果时就弹出toast提示
     */
    var toast = true

    open fun success(t: T) {

    }

    open fun failed(message: String) {

    }

    open fun noNetwork() {

    }
}