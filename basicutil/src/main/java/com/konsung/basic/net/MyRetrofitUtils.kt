package com.konsung.basic.net

import android.content.Context
import androidx.annotation.StringRes
import com.konsung.basic.bean.*
import com.konsung.basic.bean.navigation.NavigationBean
import com.konsung.basic.bean.project.ProjectTitleBean
import com.konsung.basic.bean.search.SearchKey
import com.konsung.basic.bean.site.SiteCollectBean
import com.konsung.basic.bean.tree.SystemTreeListBean
import com.konsung.basic.config.BaseConfig
import com.konsung.basic.config.NoNetworkException
import com.konsung.basic.config.RequestResult
import com.konsung.basic.net.cookie.PersistentCookieJar
import com.konsung.basic.util.AppUtils
import com.konsung.basic.util.Debug
import com.konsung.basic.util.R
import com.konsung.basic.util.ToastUtils
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.ref.WeakReference
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import javax.net.ssl.SSLHandshakeException


class MyRetrofitUtils private constructor() {

    private var retrofit: Retrofit

    companion object {
        val TAG: String = MyRetrofitUtils::class.java.simpleName
        const val CONNECT_TIME_OUT = 15L
        val instance by lazy { MyRetrofitUtils() }
    }

    init {

        val context = AppUtils.getContext()

        Debug.info(TAG, "MyRetrofitUtils context=$context")

        val httpLoggingInterceptor = HttpLoggingInterceptor()
        if (AppUtils.isDebug()) {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }

        val net = object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): okhttp3.Response {

                if (NetworkStatusCallback.mType == NetworkType.NETWORK_NO) {
                    throw NoNetworkException()
                }

                val request = chain.request()
                Debug.info(TAG, "MyRetrofitUtils intercept request=$request")
                return chain.proceed(request)
            }
        }

        val cookieJar = PersistentCookieJar(context)

        val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(net)
                .cookieJar(cookieJar)
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .build()

        retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BaseConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    private fun getRetrofit(): InfoApi {
        return retrofit.create(InfoApi::class.java)
    }

    fun addSite(context: Context, name: String, link: String, result: RequestResult<SiteCollectBean>) {
        getRetrofit()
                .addSite(name, link)
                .enqueue(CallInterceptor(context, result))
    }

    fun collect(context: Context, id: Int, result: RequestResult<String>) {
        getRetrofit()
                .collect(id)
                .enqueue(CallInterceptor(context, result, true))
    }

    fun collectLink(context: Context, title: String, author: String, link: String, result: RequestResult<HomeData.DatasBean>) {
        getRetrofit()
                .collectLink(title, author, link)
                .enqueue(CallInterceptor(context, result))
    }

    fun editSite(context: Context, id: Int, name: String, link: String, result: RequestResult<SiteCollectBean>) {
        getRetrofit()
                .editSite(id, name, link)
                .enqueue(CallInterceptor(context, result))
    }

    fun fetchCollectList(context: Context, page: Int, result: RequestResult<HomeData>) {
        getRetrofit()
                .fetchCollectList(page)
                .enqueue(CallInterceptor(context, result))
    }

    fun loadBanner(context: Context, result: RequestResult<List<BannerData>>) {
        getRetrofit()
                .loadBanner()
                .enqueue(CallInterceptor(context, result))
    }

    fun fetNavigationList(context: Context, result: RequestResult<List<NavigationBean>>) {
        getRetrofit()
                .fetNavigationList()
                .enqueue(CallInterceptor(context, result))
    }

    fun fetchNewestProject(context: Context, page: Int, result: RequestResult<HomeData>) {
        getRetrofit()
                .fetchNewestProject(page)
                .enqueue(CallInterceptor(context, result))
    }

    fun fetchProjectTree(context: Context, page: Int, cId: Int, result: RequestResult<HomeData>) {
        getRetrofit()
                .fetchProjectTree(page, cId)
                .enqueue(CallInterceptor(context, result))
    }

    fun fetchTreeList(context: Context, result: RequestResult<List<SystemTreeListBean>>) {
        getRetrofit()
                .fetchTreeList()
                .enqueue(CallInterceptor(context, result))
    }

    fun fetchSearchHotKey(context: Context, result: RequestResult<List<SearchKey>>) {
        getRetrofit()
                .fetchSearchHotKey()
                .enqueue(CallInterceptor(context, result))
    }

    fun fetchSearchResult(context: Context, page: Int, key: String, result: RequestResult<HomeData>) {
        getRetrofit()
                .fetchSearchResult(page, key)
                .enqueue(CallInterceptor(context, result))
    }

    fun fetchSearchResultByAuthor(context: Context, page: Int, author: String, result: RequestResult<HomeData>) {
        getRetrofit()
                .fetchSearchResultByAuthor(page, author)
                .enqueue(CallInterceptor(context, result))
    }

    fun fetchCollectSiteList(context: Context, result: RequestResult<List<SiteCollectBean>>) {
        getRetrofit()
                .fetchCollectSiteList()
                .enqueue(CallInterceptor(context, result))
    }

    fun fetchWxSearchResult(context: Context, wxId: Int, page: Int, key: String, result: RequestResult<HomeData>) {
        getRetrofit()
                .fetchWxSearchResult(wxId, page, key)
                .enqueue(CallInterceptor(context, result))
    }

    fun fetchSystemTreeDetail(context: Context, page: Int, cid: Int, result: RequestResult<HomeData>) {
        getRetrofit()
                .fetchSystemTreeDetail(page, cid)
                .enqueue(CallInterceptor(context, result))
    }

    fun fetchWxArticleTitle(context: Context, result: RequestResult<List<ProjectTitleBean>>) {
        getRetrofit()
                .fetchWxArticleTitle()
                .enqueue(CallInterceptor(context, result))
    }

    fun fetchWxArticleDetail(context: Context, cId: Int, page: Int, result: RequestResult<HomeData>) {
        getRetrofit()
                .fetchWxArticleDetail(cId, page)
                .enqueue(CallInterceptor(context, result))
    }

    fun loadCommonWeb(context: Context, result: RequestResult<List<CommonWebBean>>) {
        getRetrofit()
                .loadCommonWeb()
                .enqueue(CallInterceptor(context, result))
    }

    fun loadHome(context: Context, page: Int, result: RequestResult<HomeData>) {
        getRetrofit()
                .loadHomeData(page)
                .enqueue(CallInterceptor(context, result))
    }

    fun loadProjectTitle(context: Context, result: RequestResult<List<ProjectTitleBean>>) {
        getRetrofit()
                .loadProjectTitle()
                .enqueue(CallInterceptor(context, result))
    }

    fun loadTopArticle(context: Context, result: RequestResult<List<HomeData.DatasBean>>) {
        getRetrofit()
                .loadTopArticle()
                .enqueue(CallInterceptor(context, result))
    }

    fun register(context: Context, userName: String, password1: String, password2: String, result: RequestResult<UserDto>) {
        getRetrofit()
                .register(userName, password1, password2)
                .enqueue(CallInterceptor(context, result))
    }

    fun login(context: Context, userName: String, passWord: String, result: RequestResult<UserDto>) {
        getRetrofit()
                .login(userName, passWord)
                .enqueue(CallInterceptor(context, result))
    }

    fun logout(context: Context, result: RequestResult<String>) {
        getRetrofit()
                .logout()
                .enqueue(CallInterceptor(context, result, true))
    }

    fun unCollect(context: Context, id: Int, result: RequestResult<String>) {
        getRetrofit()
                .unCollect(id)
                .enqueue(CallInterceptor(context, result, true))
    }

    fun unCollectInList(context: Context, id: Int, originId: Int, result: RequestResult<String>) {
        getRetrofit()
                .unCollectInList(id, originId)
                .enqueue(CallInterceptor(context, result, true))
    }
}

/**
 * 对请求的数据进一步处理
 * @param dataWillNull 返回的data是否为null
 */
class CallInterceptor<T>(context: Context, private val result: RequestResult<T>, private val dataWillNull: Boolean = false) : Callback<BasicData<T>> {

    companion object {
        val TAG: String = CallInterceptor::class.java.simpleName
    }

    private val ctxReference: WeakReference<Context> = WeakReference(context)

    override fun onFailure(call: Call<BasicData<T>>, t: Throwable) {

        Debug.info(TAG, "CallInterceptor onFailure t=$t")

        if (t is NoNetworkException) {
            failed(R.string.network_is_not_connected, toFailed = false)
            if (!result.stop) {
                result.noNetwork()
            }
            return
        }

        if (t is TimeoutException || t is SocketTimeoutException || t is SocketException) {
            failed(R.string.network_is_timeout)
            return
        }

        if (t is SSLHandshakeException) {
            failed(R.string.abnormal_security_certificate)
            return
        }

        if (t is UnknownHostException) {
            failed(R.string.domain_resolution_failed)
            return
        }

        if (t is HttpException) {
            when (t.code()) {
                504 -> failed(R.string.network_exception_please_check_your_network_status)
                404 -> failed(R.string.invalid_network_address)
                401 -> failed(R.string.authentication_failed)
                else -> failed(R.string.data_request_failed)
            }

            return
        }

        failed(R.string.data_request_failed)
    }

    override fun onResponse(call: Call<BasicData<T>>, response: Response<BasicData<T>>) {

        if (!response.isSuccessful) {
            failed(R.string.data_request_error)
            return
        }

        val data = response.body()
        if (data == null) {
            failed(R.string.data_request_error)
            return
        }

        if (data.errorCode != 0) {

            val info: String? = if (data.errorCode == -1001) {
                //未登录
                ctxReference.get()?.getString(R.string.please_login_first)
            } else {
                data.errorMsg
            }

            val toast = info ?: ctxReference.get()?.getString(R.string.data_request_error)

            failed(toast ?: "")
            return
        }

        if (result.stop) {
            return
        }

        if (dataWillNull) {
            result.complete(true)
            result.success()
            return
        }

        val resultData = data.data
        if (resultData == null) {
            failed(R.string.data_request_error)
            return
        }

        result.complete(true)
        result.success(resultData)
    }

    private fun failed(@StringRes textRes: Int, toFailed: Boolean = true) {

        try {
            val text = ctxReference.get()?.getString(textRes)
            failed(text ?: "", toFailed)
        } catch (e: Exception) {
        }
    }

    private fun failed(text: String, toFailed: Boolean = true) {

        Debug.info(TAG, "failed text=$text toFailed=$toFailed")

        if (result.stop) {
            return
        }

        if (result.toast) {
            ctxReference.get()?.let {
                ToastUtils.instance.toast(it, TAG, text)
            }
        }

        result.complete(false)
        if (toFailed) {
            result.failed(text)
        }
    }
}
