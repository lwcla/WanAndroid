package com.konsung.basic.presenter

import android.content.Context

interface UiView {

    fun getUiContext(): Context?

    fun loadComplete(success: Boolean) {
        //默认空实现
    }

    fun showContentView() {
        //默认空实现
    }

    fun showErrorView() {
        //默认空实现
    }

    fun showNoNetworkView() {
        //默认空实现
    }

    fun showLoadView() {
        //默认空实现
    }

    fun showEmptyView() {
        //默认空实现
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