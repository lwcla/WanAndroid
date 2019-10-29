package com.konsung.basic.presenter

import android.content.Context
import com.konsung.basic.config.RequestData
import com.konsung.basic.model.BaseModel
import com.konsung.basic.model.Model
import com.konsung.basic.net.cookie.PersistentCookieJar

/**
 * 退出登录presenter实现类
 */
class LogoutPresenterImpl(uiView: LogoutView?) : BasePresenter1<String, LogoutView, LogoutModel>(uiView, LogoutModelImpl()), LogoutPresenter {

    override fun success(refreshData: Boolean) {
        getContext()?.let {
            val cookieJar = PersistentCookieJar(it)
            cookieJar.clear()
            getUiView()?.logoutResult(true, "")
        }
    }

    override fun failed(message: String, refreshData: Boolean) {
        getUiView()?.logoutResult(false, message)
    }

    override fun logout() {
        request { context, _, requestData -> model.logout(context, requestData) }
    }
}

/**
 * LogoutMode实现类
 */
class LogoutModelImpl : BaseModel<String>(), LogoutModel {
    override fun logout(context: Context?, requestData: RequestData<String>) {
        request(context, requestData) { ctx, result ->
            httpHelper.logout(ctx, result)
        }
    }
}

/**
 * 退出登录Presenter
 */
interface LogoutPresenter : Presenter {
    /**
     * 退出登录
     */
    fun logout()
}

interface LogoutModel : Model {
    /**
     * 退出登录
     */
    fun logout(context: Context?, requestData: RequestData<String>)
}

interface LogoutView : UiView {
    /**
     * 退出登录结果
     * @param success 成功或者失败
     * @param errorInfo 错误信息
     */
    fun logoutResult(success: Boolean, errorInfo: String)
}