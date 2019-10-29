package com.konsung.basic.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.konsung.basic.presenter.BasicPresenter
import com.konsung.basic.presenter.Presenter
import com.konsung.basic.presenter.UiView

/**
 * MvpFragment
 */
abstract class MvpFragment : Fragment(), UiView {

    private var presenter: List<BasicPresenter>? = null
    private var presenterList: List<Presenter>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = initPresenters()
        presenterList = initPresenterList()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.let {
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

    open fun initPresenters(): List<BasicPresenter>? = null

    abstract fun initPresenterList(): List<Presenter>?
}