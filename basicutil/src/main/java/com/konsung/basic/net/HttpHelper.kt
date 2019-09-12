package com.konsung.basic.net

import android.content.Context
import com.konsung.basic.bean.BannerData
import com.konsung.basic.bean.CommonWebBean
import com.konsung.basic.bean.HomeData
import com.konsung.basic.bean.UserDto
import com.konsung.basic.bean.project.ProjectBean
import com.konsung.basic.bean.project.ProjectTitleBean
import com.konsung.basic.config.RequestResult

interface HttpHelper {

    /**
     * 收藏
     */
    fun collect(context: Context, id: Int, result: RequestResult<String>)

    /**
     * 最新项目
     */
    fun fetchNewestProject(context: Context, page: Int, result: RequestResult<ProjectBean>)

    /**
     * 项目列表数据
     */
    fun fetchProjectTree(context: Context, page: Int, cId: Int, result: RequestResult<ProjectBean>)

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
     * 注册
     */
    fun register(context: Context, userName: String, password1: String, password2: String, result: RequestResult<UserDto>)

    /**
     * 取消收藏
     */
    fun unCollect(context: Context, id: Int, result: RequestResult<String>)

}

class HttpHelperImpl {
    companion object {
        fun create(): HttpHelper {
            return RequestUtils.instance
        }
    }
}