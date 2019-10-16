package com.cla.home.main

import android.content.Context
import com.konsung.basic.bean.BannerData
import com.konsung.basic.ui.BasePresenter2
import com.konsung.basic.ui.BasicView
import com.konsung.basic.ui.UiView

class BannerPresenter(uiView: UiView?, view: LoadBannerView?) : BasePresenter2<List<BannerData>, LoadBannerView>(uiView, view) {

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