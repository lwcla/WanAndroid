package com.konsung.basic.net

import android.content.Context
import com.konsung.basic.bean.BannerData
import com.konsung.basic.bean.CommonWebBean
import com.konsung.basic.bean.HomeData
import com.konsung.basic.bean.UserDto
import com.konsung.basic.bean.project.ProjectBean
import com.konsung.basic.bean.project.ProjectTitleBean
import com.konsung.basic.config.RequestResult

class RequestUtils private constructor() : HttpHelper {
    companion object {

        val instance = RequestUtils()

    }

    private val request = MyRetrofitUtils.instance
    override fun collect(context: Context, id: Int, result: RequestResult<String>) {
        request.collect(context, id, result)
    }

    override fun fetchNewestProject(context: Context, page: Int, result: RequestResult<ProjectBean>) {
        request.fetchNewestProject(context, page, result)
    }

    override fun fetchProjectTree(context: Context, page: Int, cId: Int, result: RequestResult<ProjectBean>) {
        request.fetchProjectTree(context, page, cId, result)
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
}