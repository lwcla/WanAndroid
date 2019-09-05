package com.konsung.cla.demo2.main.fragment.home

import android.content.Context
import com.konsung.basic.bean.BannerData
import com.konsung.basic.ui.BasePresenter
import com.konsung.basic.ui.BasicView

class BannerPresenter(view: LoadBannerView?) : BasePresenter<List<BannerData>, LoadBannerView>(view) {

    fun load(context: Context?) {
        request(context) { ctx, result ->
            httpHelper.loadBanner(ctx, result)
        }
    }

}

open class LoadBannerView : BasicView<List<BannerData>>()