package com.konsung.basic.ui

abstract class BasicPresenter {
    abstract fun destroy()
}

abstract class BasicView<T> {

    open fun success(t: T) {

    }

    open fun failed(string: String) {

    }

    open fun noNetwork() {

    }
}