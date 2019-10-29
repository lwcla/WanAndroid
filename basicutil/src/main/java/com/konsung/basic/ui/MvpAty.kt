package com.konsung.basic.ui

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.konsung.basic.presenter.BasicPresenter
import com.konsung.basic.presenter.Presenter
import com.konsung.basic.presenter.UiView
import com.konsung.basic.util.Debug

abstract class MvpAty : AppCompatActivity(), UiView {

    companion object {
        val TAG: String = MvpAty::class.java.simpleName
    }

    protected lateinit var context: Context

    private var presenters: List<BasicPresenter>? = null
    private var presenterList: List<Presenter>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        presenters = initPresenter()
        presenterList = initPresenterList()
    }

    override fun onDestroy() {
        super.onDestroy()
        Debug.info(BasicAty.TAG, "onDestroy $this")
        presenters?.let {
            for (p in it) {
                p.destroy()
            }
        }

        presenterList?.let {
            for (p in it) {
                p.destroy()
            }
        }
    }

    override fun getUiContext(): Context? = context

    override fun loadComplete(success: Boolean) {

    }

    override fun showContentView() {

    }

    override fun showErrorView() {

    }

    override fun showNoNetworkView() {

    }

    override fun showLoadView() {

    }

    override fun showEmptyView() {

    }

    open fun initPresenter(): List<BasicPresenter>? = null

    abstract fun initPresenterList(): List<Presenter>?
}