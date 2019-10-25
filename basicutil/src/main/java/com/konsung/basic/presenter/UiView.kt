package com.konsung.basic.presenter

import android.content.Context

interface UiView {

    fun getUiContext(): Context?

    fun loadComplete(success: Boolean)

    fun showContentView()

    fun showErrorView()

    fun showNoNetworkView()

    fun showLoadView()

    fun showEmptyView()
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