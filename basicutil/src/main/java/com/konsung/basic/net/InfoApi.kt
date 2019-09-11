package com.konsung.basic.net

import com.konsung.basic.bean.*
import retrofit2.Call
import retrofit2.http.*

interface InfoApi {

    @GET("wxarticle/chapters/json")
    fun getWeChatOfficial(): Call<WeChatOfficial>

    /**
     * 广告栏
     * https://www.wanandroid.com/banner/json
     *
     * @return 广告栏数据
     */
    @GET("banner/json")
    fun loadBanner(): Call<BasicData<List<BannerData>>>

    /**
     * 常用网站
     * https://www.wanandroid.com/friend/json
     *
     * @return 常用网站
     */
    @GET("friend/json")
    fun loadCommonWeb(): Call<BasicData<List<CommonWebBean>>>

    /**
     * 获取文章列表
     * https://www.wanandroid.com/article/list/0/json
     *
     * @param page 页标
     */
    @GET("article/list/{page}/json")
    fun loadHomeData(@Path("page") page: Int): Call<BasicData<HomeData>>

    /**
     * 获取置顶文章
     * https://www.wanandroid.com/article/top/json
     */
    @GET("article/top/json")
    fun loadTopArticle(): Call<BasicData<List<HomeData.DatasBean>>>

    /**
     * 注册
     * https://www.wanandroid.com/user/register
     *
     * @param username   user name
     * @param password   password
     * @param repassword re password
     * @return 注册数据
     */
    @POST("user/register")
    @FormUrlEncoded
    fun register(@Field("username") username: String, @Field("password") password: String, @Field("repassword") repassword: String): Call<BasicData<UserDto>>

    /**
     * 登录
     * https://www.wanandroid.com/user/login
     *
     * @param username user name
     * @param password password
     * @return 登录数据
     */
    @POST("user/login")
    @FormUrlEncoded
    fun login(@Field("username") username: String, @Field("password") password: String): Call<BasicData<UserDto>>

    /**
     * 收藏站内文章
     * https://www.wanandroid.com/lg/collect/1165/json
     *
     * @param id article id
     * @return 收藏站内文章数据
     */
    @POST("lg/collect/{id}/json")
    fun collect(@Path("id") id: Int): Call<BasicData<String>>

    /**
     * 取消收藏
     * https://www.wanandroid.com/lg/uncollect_originId/2333/json
     *
     * @param id article id
     * @return 取消收藏站内文章数据
     */
    @POST("lg/uncollect_originId/{id}/json")
    fun unCollect(@Path("id") id: Int): Call<BasicData<String>>

}