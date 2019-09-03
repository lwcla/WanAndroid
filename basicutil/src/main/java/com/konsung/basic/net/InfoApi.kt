package com.konsung.basic.net

import com.konsung.basic.bean.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface InfoApi {

    @GET("wxarticle/chapters/json")
    fun getWeChatOfficial(): Call<WeChatOfficial>

    @GET("banner/json")
    fun loadBanner(): Call<BasicData<List<BannerData>>>

    @GET("article/list/{page}/json")
    fun loadHomeData(@Path("page") page: Int): Call<BasicData<HomeData>>

    @POST("user/register")
    fun register(@Query("username") username: String, @Query("password") password: String, @Query("repassword") repassword: String): Call<BasicData<UserDto>>

    @POST("user/login")
    fun login(@Query("username") username: String, @Query("password") password: String)

}