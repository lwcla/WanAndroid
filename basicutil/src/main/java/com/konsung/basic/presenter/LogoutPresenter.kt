package com.konsung.basic.presenter

import android.content.Context
import com.konsung.basic.config.BaseConfig
import com.konsung.basic.net.cookie.PersistentCookieJar
import com.konsung.basic.ui.BasePresenter2
import com.konsung.basic.ui.BasicView
import com.konsung.basic.ui.UiView
import com.konsung.basic.util.R
import com.konsung.basic.util.SpUtils
import com.konsung.basic.util.toast

class LogoutPresenter(uiView: UiView?, view: LogoutView) : BasePresenter2<String, LogoutView>(uiView, view) {

    companion object {
        val TAG: String = LogoutPresenter::class.java.simpleName
    }

    override fun success(context: Context) {
        val cookieJar = PersistentCookieJar(context)
        cookieJar.clear()
        context.toast(TAG, R.string.logout_success)
        super.success(context)
    }

    fun logout() {

        val context = getUiView()?.getUiContext() ?: return
        val userName = SpUtils.getString(context, BaseConfig.USER_NAME, "")

        if (userName.isNullOrEmpty()) {
            //现在是未登陆状态
            return
        }

        request { ctx, result ->
            httpHelper.logout(ctx, result)
        }
    }
}

open class LogoutView : BasicView<String>()