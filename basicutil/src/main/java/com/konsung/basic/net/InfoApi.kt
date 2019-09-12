package com.konsung.basic.net

import com.konsung.basic.bean.*
import com.konsung.basic.bean.project.ProjectBean
import com.konsung.basic.bean.project.ProjectTitleBean
import retrofit2.Call
import retrofit2.http.*

interface InfoApi {

    @GET("wxarticle/chapters/json")
    fun getWeChatOfficial(): Call<WeChatOfficial>

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
     * 最新项目tab
     * 按时间分页展示所有项目
     * https://wanandroid.com/article/listproject/0/json
     *
     * @param page 页码
     */
    @GET("article/listproject/{page}/json")
    fun fetchNewestProject(@Path("page") page: Int): Call<BasicData<ProjectBean>>

    /**
     * 项目列表数据
     * 某一个分类下项目列表数据，分页展示
     * https://www.wanandroid.com/project/list/1/json?cid=294
     *
     * @param cId 分类的id，上面项目分类接口
     * @param page 页码，从1开始
     */
    @GET("project/list/{page}/json")
    fun fetchProjectTree(@Path("page") page: Int, @Query("cid") cId: Int): Call<BasicData<ProjectBean>>

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
     * 项目分类
     * https://www.wanandroid.com/project/tree/json
     */
    @GET("project/tree/json")
    fun loadProjectTitle(): Call<BasicData<List<ProjectTitleBean>>>

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
     * 取消收藏
     * https://www.wanandroid.com/lg/uncollect_originId/2333/json
     *
     * @param id article id
     * @return 取消收藏站内文章数据
     */
    @POST("lg/uncollect_originId/{id}/json")
    fun unCollect(@Path("id") id: Int): Call<BasicData<String>>

}