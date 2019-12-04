package com.konsung.basic.presenter

import android.content.Context

interface UiView {

    fun getUiContext(): Context?

    fun loadComplete(success: Boolean) {
        //空实现
    }

    fun showContentView() {
        //空实现
    }

    fun showErrorView() {
        //空实现
    }

    fun showNoNetworkView() {
        //空实现
    }

    fun showLoadView() {
        //空实现
    }

    fun showEmptyView() {
        //空实现
    }
}

abstract class UiViewAdapter : UiView {

    override fun getUiContext(): Context? = null

    override fun loadComplete(success: Boolean) {

    }

    override fun showContentView() {

    }

    override fun showLoadView() {

    }

    override fun showErrorView() {

    }

    override fun showNoNetworkView() {

    }

    override fun showEmptyView() {

    }
}