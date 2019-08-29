package com.konsung.basic.net

import android.content.Context
import androidx.annotation.StringRes
import com.konsung.basic.bean.BannerData
import com.konsung.basic.bean.BasicData
import com.konsung.basic.bean.HomeData
import com.konsung.basic.bean.WeChatOfficial
import com.konsung.basic.config.NoNetworkException
import com.konsung.basic.config.RequestResult
import com.konsung.basic.util.AppUtils
import com.konsung.basic.util.Debug
import com.konsung.basic.util.R
import com.konsung.basic.util.ToastUtils
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import okio.Buffer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.URLDecoder
import java.util.concurrent.TimeoutException


class MyRetrofitUtils private constructor() {

    private var retrofit: Retrofit? = null

    companion object {
        const val BASE_URL = "https://www.wanandroid.com/"
        val TAG: String = MyRetrofitUtils::class.java.simpleName
        val instance by lazy { MyRetrofitUtils() }
    }

    init {

    }

    private fun getRetrofit(): InfoApi {
        if (retrofit == null) {

            val httpLoggingInterceptor = HttpLoggingInterceptor()
            if (AppUtils.isDebug()) {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            }

            val net = object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): okhttp3.Response {

                    if (NetChangeReceiver.mType == NetworkType.NETWORK_NO) {
                        throw NoNetworkException()
                    }

                    return chain.proceed(chain.request())
                }
            }

//            val charsetInterceptor = object : Interceptor {
//                override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
//
//                    var request = chain.request()
//                    val requestBuilder = request.newBuilder()
//                    request = requestBuilder.post(URLDecoder.decode(bodyToString(request.body), "UTF-8").toRequestBody("application/x-www-form-urlencoded;charset=GBK".toMediaTypeOrNull())).build()
//                    return chain.proceed(request)
//                }
//            }

            val okHttpClient = OkHttpClient.Builder()
//                    .addInterceptor(charsetInterceptor)
                    .addInterceptor(httpLoggingInterceptor)
                    .addInterceptor(net)
                    .build()

            retrofit = Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }

        return retrofit!!.create(InfoApi::class.java)
    }

    private fun bodyToString(request: RequestBody?): String {
        try {
            val buffer = Buffer()
            if (request != null)
                request.writeTo(buffer)
            else
                return ""
            return buffer.readUtf8()
        } catch (e: IOException) {
            return "did not work"
        }
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
        val call = getRetrofit().loadBanner()
        call.enqueue(CallInterceptor(context, result))
    }

    fun loadHome(context: Context, page: Int, result: RequestResult<HomeData>) {
        val call = getRetrofit().loadHomeData(page)
        call.enqueue(CallInterceptor(context, result))
    }
}

/**
 * 对请求的数据进一步处理
 */
class CallInterceptor<T>(private val context: Context, private val result: RequestResult<T>) : Callback<BasicData<T>> {

    companion object {
        val TAG: String = CallInterceptor::class.java.simpleName
    }

    override fun onFailure(call: Call<BasicData<T>>, t: Throwable) {

        Debug.info(TAG, "CallInterceptor onFailure t=$t")

        if (t is NoNetworkException) {
            failed(R.string.network_is_not_connected)
            result.noNetwork()
            return
        }

        if (t is TimeoutException) {
            failed(R.string.network_is_timeout)
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

        if (result.toast) {
            ToastUtils.instance.toast(context, TAG, text)
        }

        result.failed(text)
    }
}
