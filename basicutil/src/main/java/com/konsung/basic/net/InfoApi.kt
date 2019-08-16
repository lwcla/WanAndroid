package com.konsung.basic.net

import com.konsung.basic.bean.BannerData
import com.konsung.basic.bean.BasicData
import com.konsung.basic.bean.HomeData
import com.konsung.basic.bean.WeChatOfficial
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface InfoApi {

    @GET("wxarticle/chapters/json")
    fun getWeChatOfficial(): Call<WeChatOfficial>

    @GET("banner/json")
    fun loadBanner(): Call<BasicData<List<BannerData>>>

    @GET("article/list/{page}/json")
    fun loadHomeData(@Path("page") page: Int): Call<BasicData<HomeData>>

}