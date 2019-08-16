package com.konsung.cla.demo2.presenter

import android.content.Context
import com.konsung.basic.bean.BannerData
import com.konsung.basic.config.RequestResult
import com.konsung.basic.ui.BasicPresenter
import com.konsung.basic.ui.BasicView
import com.konsung.basic.util.RequestUtils

class BannerPresenter(var view: LoadBannerView?) : BasicPresenter() {


    fun load(context: Context?) {

        val ctx = context ?: return

        val result = object : RequestResult<List<BannerData>>() {

            override fun success(t: List<BannerData>) {
                view?.success(t)
            }
        }

        RequestUtils.instance.loadBanner(ctx, result)

    }

    override fun destroy() {
        view = null
    }
}

open class LoadBannerView : BasicView<List<BannerData>>()