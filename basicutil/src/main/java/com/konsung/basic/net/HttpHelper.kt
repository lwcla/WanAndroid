package com.konsung.basic.net

import android.content.Context
import com.konsung.basic.bean.BannerData
import com.konsung.basic.bean.CommonWebBean
import com.konsung.basic.bean.HomeData
import com.konsung.basic.bean.UserDto
import com.konsung.basic.bean.navigation.NavigationBean
import com.konsung.basic.bean.project.ProjectTitleBean
import com.konsung.basic.bean.search.SearchKey
import com.konsung.basic.bean.site.SiteCollectBean
import com.konsung.basic.bean.tree.SystemTreeListBean
import com.konsung.basic.config.RequestData
import com.konsung.basic.config.RequestResult

class HttpHelperImpl {
    companion object {
        fun create(): HttpHelper {
            return RequestUtils.instance
        }
    }
}

interface HttpHelper {

    /**
     * 网址收藏
     */
    fun addSite(context: Context, name: String, link: String, result: RequestResult<SiteCollectBean>)

    /**
     * 收藏
     */
    fun collect(context: Context, id: Int, result: RequestResult<String>)

    /**
     * 收藏站外文章
     */
    fun collectLink(context: Context, title: String, author: String, link: String, result: RequestResult<HomeData.DatasBean>)

    /**
     * 编辑收藏的网站
     */
    fun editSite(context: Context, id: Int, name: String, link: String, result: RequestResult<SiteCollectBean>)

    /**
     * 我的收藏列表
     */
    fun fetchCollectList(context: Context, page: Int, result: RequestResult<HomeData>)

    /**
     * 导航
     */
    fun fetNavigationList(context: Context, result: RequestResult<List<NavigationBean>>)

    /**
     * 最新项目
     */
    fun fetchNewestProject(context: Context, page: Int, result: RequestResult<HomeData>)

    /**
     * 项目列表数据
     */
    fun fetchProjectTree(context: Context, page: Int, cId: Int, result: RequestResult<HomeData>)

    /**
     * 搜索热词
     */
    fun fetchSearchHotKey(context: Context, result: RequestResult<List<SearchKey>>)

    /**
     * 搜索
     */
    fun fetchSearchResult(context: Context, page: Int, key: String, result: RequestResult<HomeData>)

    /**
     * 选择用作者昵称筛选
     */
    fun fetchSearchResultByAuthor(context: Context, page: Int, author: String, result: RequestResult<HomeData>)

    /**
     * 收藏网站列表
     */
    fun fetchCollectSiteList(context: Context, result: RequestResult<List<SiteCollectBean>>)

    /**
     * 在某个公众号中搜索历史文章
     */
    fun fetchWxSearchResult(context: Context, wxId: Int, page: Int, key: String, result: RequestResult<HomeData>)

    /**
     * 知识体系下的文章
     */
    fun fetchSystemTreeDetail(context: Context, page: Int, cid: Int, result: RequestResult<HomeData>)

    /**
     * 体系分类列表
     */
    fun fetchTreeList(context: Context, result: RequestResult<List<SystemTreeListBean>>)

    /**
     * 获取公众号列表
     */
    fun fetchWxArticleTitle(context: Context, result: RequestResult<List<ProjectTitleBean>>)

    /**
     * 查看某个公众号历史数据
     */
    fun fetchWxArticleDetail(context: Context, cId: Int, page: Int, result: RequestResult<HomeData>)

    /**
     * 广告
     */
    fun loadBanner(context: Context, result: RequestResult<List<BannerData>>)

    /**
     * 常用网站
     */
    fun loadCommonWeb(context: Context, result: RequestResult<List<CommonWebBean>>)

    /**
     * 首页数据
     */
    fun loadHomeData(context: Context, page: Int, result: RequestResult<HomeData>)

    /**
     * 项目分类标题
     */
    fun loadProjectTitle(context: Context, result: RequestResult<List<ProjectTitleBean>>)

    /**
     * 置顶文章
     */
    fun loadTopArticle(context: Context, result: RequestResult<List<HomeData.DatasBean>>)

    /**
     * 登录
     */
    fun login(context: Context, userName: String, passWord: String, result: RequestResult<UserDto>)

    /**
     * 登录
     */
    fun login(context: Context, userName: String, passWord: String, result: RequestData<UserDto>)

    /**
     * 退出登录
     */
    fun logout(context: Context, result: RequestResult<String>)

    /**
     * 退出登录
     */
    fun logout(context: Context, result: RequestData<String>)

    /**
     * 注册
     */
    fun register(context: Context, userName: String, password1: String, password2: String, result: RequestResult<UserDto>)

    /**
     * 注册
     */
    fun register(context: Context, userName: String, password1: String, password2: String, result: RequestData<UserDto>)

    /**
     * 取消收藏
     */
    fun unCollect(context: Context, id: Int, result: RequestResult<String>)

    /**
     * 在收藏列表取消收藏
     */
    fun unCollectInList(context: Context, id: Int, originId: Int, result: RequestResult<String>)
}
