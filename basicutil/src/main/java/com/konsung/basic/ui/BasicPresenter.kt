package com.konsung.basic.ui

abstract class BasicPresenter {
    abstract fun destroy()

    public open fun stop() {}
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

    open fun complete(){

    }
}