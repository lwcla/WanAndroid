package com.konsung.cla.demo2.main.fragment.home

import android.content.Context
import com.konsung.basic.bean.BannerData
import com.konsung.basic.ui.BasePresenter2
import com.konsung.basic.ui.BasicView

open class BannerPresenter(context: Context?, view: LoadBannerView?) : BasePresenter2<List<BannerData>, LoadBannerView>(context, view) {

    fun load() {
        request { ctx, result ->
            httpHelper.loadBanner(ctx, result)
        }
    }

}

open class LoadBannerView : BasicView<List<BannerData>>()