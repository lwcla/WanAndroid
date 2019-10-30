package com.konsung.basic.presenter

import android.content.Context
import androidx.annotation.StringRes
import com.konsung.basic.config.RequestData
import com.konsung.basic.model.BaseModel
import com.konsung.basic.model.Model
import com.konsung.basic.net.cookie.PersistentCookieJar
import com.konsung.basic.util.R

/**
 * 退出登录presenter实现类
 */
class LogoutPresenterImpl(uiView: LogoutView?) : BasePresenter1<String, LogoutView, LogoutModel>(uiView, LogoutModelImpl()), LogoutPresenter {

    override fun success(refreshData: Boolean) {
        getContext()?.let {
            val cookieJar = PersistentCookieJar(it)
            cookieJar.clear()
            getUiView()?.logoutSuccess()
        }
    }

    override fun failed(message: String, refreshData: Boolean) {
        getUiView()?.logoutFailed(message)
    }

    override fun logout() {
        request { context, _, requestData ->
            getUiView()?.showTipDialog(R.string.logout_please_wait)
            model.logout(context, requestData)
        }
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
     * 退出登录成功
     */
    fun logoutSuccess()

    /**
     * 退出登录失败
     */
    fun logoutFailed(error: String)

    /**
     * 显示提示弹窗
     */
    fun showTipDialog(@StringRes info: Int)
}