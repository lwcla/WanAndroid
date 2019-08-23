package com.konsung.cla.demo2.main.fragment.home

import android.content.Context
import com.konsung.basic.bean.HomeData
import com.konsung.basic.config.RequestResult
import com.konsung.basic.ui.BasicPresenter
import com.konsung.basic.ui.BasicView
import com.konsung.basic.util.RequestUtils

class HomeDataPresenter(private var view: LoadHomeView?) : BasicPresenter() {

    fun load(context: Context?, page: Int) {

        val ctx = context ?: return

        val result = object : RequestResult<HomeData>(view) {

            override fun success(t: HomeData) {
                if (t.datas == null || t.datas!!.isEmpty()) {
                    view?.failed("")
                    return
                }

                view?.success(t)
            }
        }

        RequestUtils.instance.loadHomeData(ctx, page, result)
    }

    override fun destroy() {
        view = null
    }

}

open class LoadHomeView : BasicView<HomeData>()