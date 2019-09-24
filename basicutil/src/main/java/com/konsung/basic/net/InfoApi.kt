package com.konsung.basic.net

import com.konsung.basic.bean.*
import com.konsung.basic.bean.navigation.NavigationBean
import com.konsung.basic.bean.project.ProjectBean
import com.konsung.basic.bean.project.ProjectTitleBean
import com.konsung.basic.bean.search.SearchKey
import com.konsung.basic.bean.tree.SystemTreeListBean
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
     * 导航数据
     * https://www.wanandroid.com/navi/json
     *
     * @return 常用网站
     */
    @GET("navi/json")
    fun fetNavigationList(): Call<BasicData<List<NavigationBean>>>

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
     * 体系数据
     * https://www.wanandroid.com/tree/json
     */
    @GET("tree/json")
    fun fetchTreeList(): Call<BasicData<List<SystemTreeListBean>>>

    /**
     * 搜索热词
     * https://www.wanandroid.com/hotkey/json
     */
    @GET("hotkey/json")
    fun fetchSearchHotKey(): Call<BasicData<List<SearchKey>>>

    /**
     * 搜索
     * https://www.wanandroid.com/article/query/0/json
     */
    @POST("article/query/{page}/json")
    fun fetchSearchResult(@Path("page") page: Int, @Query("k") key: String): Call<BasicData<ProjectBean>>

    /**
     * 知识体系下的文章
     * https://www.wanandroid.com/article/list/0/json?cid=60
     */
    @GET("article/list/{page}/json")
    fun fetchSystemTreeDetail(@Path("page") page: Int, @Query("cid") cId: Int): Call<BasicData<ProjectBean>>

    /**
     * 获取公众号列表
     * https://wanandroid.com/wxarticle/chapters/json
     */
    @GET("wxarticle/chapters/json")
    fun fetchWxArticleTitle(): Call<BasicData<List<ProjectTitleBean>>>

    /**
     * 查看某个公众号历史数据
     * https://wanandroid.com/wxarticle/list/408/1/json
     *
     * @param cId 公众号 ID：拼接在 url 中，eg:405
     * @param page 公众号页码：拼接在url 中，eg:1
     */
    @GET("wxarticle/list/{id}/{page}/json")
    fun fetchWxArticleDetail(@Path("id") cId: Int, @Path("page") page: Int): Call<BasicData<ProjectBean>>

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