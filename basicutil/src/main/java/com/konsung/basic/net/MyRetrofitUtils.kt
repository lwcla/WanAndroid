package com.konsung.basic.net

import android.content.Context
import androidx.annotation.StringRes
import com.konsung.basic.bean.*
import com.konsung.basic.bean.project.ProjectBean
import com.konsung.basic.bean.project.ProjectTitleBean
import com.konsung.basic.bean.tree.SystemTreeListBean
import com.konsung.basic.config.BaseConfig
import com.konsung.basic.config.NoNetworkException
import com.konsung.basic.config.RequestResult
import com.konsung.basic.net.cookie.PersistentCookieJar
import com.konsung.basic.net.cookie.cache.SetCookieCache
import com.konsung.basic.net.cookie.persistence.SharedPrefsCookiePersistor
import com.konsung.basic.util.AppUtils
import com.konsung.basic.util.Debug
import com.konsung.basic.util.R
import com.konsung.basic.util.ToastUtils
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
import javax.net.ssl.SSLHandshakeException


class MyRetrofitUtils private constructor() {

    private var retrofit: Retrofit

    companion object {
        val TAG: String = MyRetrofitUtils::class.java.simpleName
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

                if (NetChangeReceiver.mType == NetworkType.NETWORK_NO) {
                    throw NoNetworkException()
                }

                val request = chain.request()
//                Debug.info(TAG,"MyRetrofitUtils intercept request=$request")
                return chain.proceed(request)
            }
        }

        val cookieJar = PersistentCookieJar(context, SetCookieCache(), SharedPrefsCookiePersistor(context))

        val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(net)
                .cookieJar(cookieJar)
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

    fun getWeChatOfficial() {

        val call = getRetrofit().getWeChatOfficial()

        call.enqueue(object : Callback<WeChatOfficial> {

            override fun onFailure(call: Call<WeChatOfficial>, t: Throwable) {
                println("lwl $TAG RequestUtils.onFailure")
            }

            override fun onResponse(call: Call<WeChatOfficial>, response: Response<WeChatOfficial>) {
                println("lwl $TAG RequestUtils.onResponse ${response.body().toString()}")
            }

        })
    }

    fun loadBanner(context: Context, result: RequestResult<List<BannerData>>) {
        getRetrofit()
                .loadBanner()
                .enqueue(CallInterceptor(context, result))
    }

    fun fetchNewestProject(context: Context, page: Int, result: RequestResult<ProjectBean>) {
        getRetrofit()
                .fetchNewestProject(page)
                .enqueue(CallInterceptor(context, result))
    }

    fun fetchProjectTree(context: Context, page: Int, cId: Int, result: RequestResult<ProjectBean>) {
        getRetrofit()
                .fetchProjectTree(page, cId)
                .enqueue(CallInterceptor(context, result))
    }

    fun fetchTreeList(context: Context, result: RequestResult<List<SystemTreeListBean>>) {
        getRetrofit()
                .fetchTreeList()
                .enqueue(CallInterceptor(context, result))
    }

    fun fetchSystemTreeDetail(context: Context, page: Int, cid: Int, result: RequestResult<ProjectBean>) {
        getRetrofit()
                .fetchSystemTreeDetail(page, cid)
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

    fun collect(context: Context, id: Int, result: RequestResult<String>) {
        getRetrofit()
                .collect(id)
                .enqueue(CallInterceptor(context, result, true))
    }

    fun unCollect(context: Context, id: Int, result: RequestResult<String>) {
        getRetrofit()
                .unCollect(id)
                .enqueue(CallInterceptor(context, result, true))
    }
}

/**
 * 对请求的数据进一步处理
 * @param dataWillNull 返回的data是否为null
 */
class CallInterceptor<T>(private val context: Context, private val result: RequestResult<T>, private val dataWillNull: Boolean = false) : Callback<BasicData<T>> {

    companion object {
        val TAG: String = CallInterceptor::class.java.simpleName
    }

    override fun onFailure(call: Call<BasicData<T>>, t: Throwable) {

        Debug.info(TAG, "CallInterceptor onFailure t=$t")

        if (t is NoNetworkException) {
            failed(R.string.network_is_not_connected)
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
            failed(data.errorMsg ?: context.getString(R.string.data_request_error))
            return
        }

        if (result.stop) {
            return
        }

        result.complete()

        if (dataWillNull) {
            result.success()
            return
        }

        val resultData = data.data
        if (resultData == null) {
            failed(R.string.data_request_error)
            return
        }

        result.success(resultData)
    }

    private fun failed(@StringRes textRes: Int) {

        try {
            val text = context.getString(textRes)
            failed(text)
        } catch (e: Exception) {
        }
    }

    private fun failed(text: String) {

        if (result.stop) {
            return
        }

        if (result.toast) {
            ToastUtils.instance.toast(context, TAG, text)
        }

        result.complete()
        result.failed(text)
    }
}
