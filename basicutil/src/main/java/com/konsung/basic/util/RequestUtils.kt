package com.konsung.basic.util

import android.content.Context
import com.konsung.basic.bean.BannerData
import com.konsung.basic.bean.HomeData
import com.konsung.basic.bean.UserDto
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

    fun register(context: Context, userName: String, password1: String, password2: String, result: RequestResult<UserDto>) {
        request.register(context, userName, password1, password2, result)
    }

    fun login(context: Context, userName: String, passWord: String, result: RequestResult<UserDto>) {
        request.login(context, userName, passWord, result)
    }

    fun collect(context: Context, id: Int, result: RequestResult<String>) {
        request.collect(context, id, result)
    }

}