package com.konsung.basic.util

import android.content.Context
import com.konsung.basic.bean.BannerData
import com.konsung.basic.bean.HomeData
import com.konsung.basic.config.RequestResult
import com.konsung.basic.net.MyRetrofitUtils

class RequestUtils private constructor() {

    companion object {
        val instance = RequestUtils()
    }

    private val request = MyRetrofitUtils.instance

    fun loadBanner(context: Context, result: RequestResult<List<BannerData>>) {
        request.loadBanner(context, result)
    }

    fun loadHomeData(context: Context, page: Int, result: RequestResult<HomeData>) {
        request.loadHome(context, page, result)
    }

}