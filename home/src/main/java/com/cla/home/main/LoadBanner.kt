package com.cla.home.main

import android.content.Context
import com.konsung.basic.bean.BannerData
import com.konsung.basic.presenter.BasicPresenter2
import com.konsung.basic.presenter.BasicView
import com.konsung.basic.presenter.UiView

class BannerPresenter(uiView: UiView?, view: LoadBannerView?) : BasicPresenter2<List<BannerData>, LoadBannerView>(uiView, view) {

    override fun success(context: Context, t: List<BannerData>) {
        view?.success(t, refreshData)
    }

    override fun failed(context: Context, message: String) {
        view?.failed(message)
    }

    fun load() {
        request { ctx, result ->
            httpHelper.loadBanner(ctx, result)
        }
    }

}

open class LoadBannerView : BasicView<List<BannerData>>()