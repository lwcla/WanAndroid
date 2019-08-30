package com.konsung.basic.net

import com.konsung.basic.bean.*
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface InfoApi {

    @GET("wxarticle/chapters/json")
    fun getWeChatOfficial(): Call<WeChatOfficial>

    @GET("banner/json")
    fun loadBanner(): Call<BasicData<List<BannerData>>>

    @GET("article/list/{page}/json")
    fun loadHomeData(@Path("page") page: Int): Call<BasicData<HomeData>>

    @POST("user/register")
    fun register(@Field("username") username: String, @Field("password") password: String, @Field("repassword") repassword: String): Call<BasicData<UserDto>>

}