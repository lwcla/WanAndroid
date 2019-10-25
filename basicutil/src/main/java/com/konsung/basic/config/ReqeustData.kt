package com.konsung.basic.config

abstract class RequestData<T> {

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

    /**
     * 刷新数据
     */
    var refreshData = true

    open fun complete(success: Boolean) {

    }

    open fun empty() {

    }

    open fun failed(message: String) {

    }

    open fun success(t: T) {

    }

    open fun success() {

    }

    open fun noNetwork() {

    }
}