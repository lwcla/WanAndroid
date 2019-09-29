package com.konsung.basic.net

import android.content.Context
import com.konsung.basic.bean.BannerData
import com.konsung.basic.bean.CommonWebBean
import com.konsung.basic.bean.HomeData
import com.konsung.basic.bean.UserDto
import com.konsung.basic.bean.navigation.NavigationBean
import com.konsung.basic.bean.project.ProjectTitleBean
import com.konsung.basic.bean.search.SearchKey
import com.konsung.basic.bean.tree.SystemTreeListBean
import com.konsung.basic.config.RequestResult

class RequestUtils private constructor() : HttpHelper {
    companion object {
        val instance by lazy { RequestUtils() }
    }

    private val request = MyRetrofitUtils.instance

    override fun collect(context: Context, id: Int, result: RequestResult<String>) {
        request.collect(context, id, result)
    }

    override fun fetchCollectList(context: Context, page: Int, result: RequestResult<HomeData>) {
        request.fetchCollectList(context, page, result)
    }

    override fun fetNavigationList(context: Context, result: RequestResult<List<NavigationBean>>) {
        request.fetNavigationList(context, result)
    }

    override fun fetchNewestProject(context: Context, page: Int, result: RequestResult<HomeData>) {
        request.fetchNewestProject(context, page, result)
    }

    override fun fetchProjectTree(context: Context, page: Int, cId: Int, result: RequestResult<HomeData>) {
        request.fetchProjectTree(context, page, cId, result)
    }

    override fun fetchTreeList(context: Context, result: RequestResult<List<SystemTreeListBean>>) {
        request.fetchTreeList(context, result)
    }

    override fun fetchSearchHotKey(context: Context, result: RequestResult<List<SearchKey>>) {
        request.fetchSearchHotKey(context, result)
    }

    override fun fetchSearchResult(context: Context, page: Int, key: String, result: RequestResult<HomeData>) {
        request.fetchSearchResult(context, page, key, result)
    }

    override fun fetchWxSearchResult(context: Context, wxId: Int, page: Int, key: String, result: RequestResult<HomeData>) {
        request.fetchWxSearchResult(context, wxId, page, key, result)
    }

    override fun fetchSystemTreeDetail(context: Context, page: Int, cid: Int, result: RequestResult<HomeData>) {
        request.fetchSystemTreeDetail(context, page, cid, result)
    }

    override fun fetchWxArticleTitle(context: Context, result: RequestResult<List<ProjectTitleBean>>) {
        request.fetchWxArticleTitle(context, result)
    }

    override fun fetchWxArticleDetail(context: Context, cId: Int, page: Int, result: RequestResult<HomeData>) {
        request.fetchWxArticleDetail(context, cId, page, result)
    }

    override fun loadBanner(context: Context, result: RequestResult<List<BannerData>>) {
        request.loadBanner(context, result)
    }

    override fun loadCommonWeb(context: Context, result: RequestResult<List<CommonWebBean>>) {
        request.loadCommonWeb(context, result)
    }

    override fun loadHomeData(context: Context, page: Int, result: RequestResult<HomeData>) {
        request.loadHome(context, page, result)
    }

    override fun loadProjectTitle(context: Context, result: RequestResult<List<ProjectTitleBean>>) {
        request.loadProjectTitle(context, result)
    }

    override fun loadTopArticle(context: Context, result: RequestResult<List<HomeData.DatasBean>>) {
        request.loadTopArticle(context, result)
    }

    override fun register(context: Context, userName: String, password1: String, password2: String, result: RequestResult<UserDto>) {
        request.register(context, userName, password1, password2, result)
    }

    override fun login(context: Context, userName: String, passWord: String, result: RequestResult<UserDto>) {
        request.login(context, userName, passWord, result)
    }

    override fun unCollect(context: Context, id: Int, result: RequestResult<String>) {
        request.unCollect(context, id, result)
    }

    override fun unCollectInList(context: Context, id: Int, originId: Int, result: RequestResult<String>) {
        request.unCollectInList(context, id, originId, result)
    }
}