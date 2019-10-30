package com.konsung.cla.demo2.presenter

import android.content.Context
import androidx.annotation.StringRes
import com.konsung.basic.bean.UserDto
import com.konsung.basic.config.BaseConfig
import com.konsung.basic.config.RequestData
import com.konsung.basic.model.BaseModel
import com.konsung.basic.model.Model
import com.konsung.basic.presenter.BasePresenter1
import com.konsung.basic.presenter.Presenter
import com.konsung.basic.presenter.UiView
import com.konsung.basic.util.SpUtils
import com.konsung.cla.demo2.R
import com.konsung.cla.demo2.aty.LoginAty

/**
 * 登录presenter实现类
 */
class LoginPresenterImpl(view: LoginView) : BasePresenter1<UserDto, LoginView, LoginModel>(view, LoginModelImpl()), LoginPresenter {


    override fun success(t: UserDto, refreshData: Boolean) {
        getContext()?.let {
            SpUtils.putString(it, BaseConfig.USER_NAME, t.username)
            getUiView()?.loginSuccess()
        }
    }

    override fun failed(message: String, refreshData: Boolean) {
        getUiView()?.loginFailed(message)
    }

    override fun login(userName: String, pass: String) {

        getUiView()?.clearErrorInfo()

        if (userName.isEmpty()) {
            getUiView()?.showErrorInfo(LoginAty.LoginInputType.USER, R.string.account_can_not_be_empty)
            return
        }

        if (pass.isEmpty()) {
            getUiView()?.showErrorInfo(LoginAty.LoginInputType.PASS, R.string.pass_word_can_not_be_empty)
            return
        }

        request { ctx, _, result ->
            getUiView()?.showTipDialog(R.string.login_please_wait)
            model.login(ctx, userName, pass, result)
        }
    }
}

/**
 * 登录Model实现类
 */
class LoginModelImpl : BaseModel<UserDto>(), LoginModel {

    override fun login(context: Context?, userName: String, pass: String, result: RequestData<UserDto>) {
        request(context, result) { ctx, requestData ->
            httpHelper.login(ctx, userName, pass, requestData)
        }
    }
}

/**
 * 登录presenter
 */
interface LoginPresenter : Presenter {
    /**
     * 登录
     */
    fun login(userName: String, pass: String)
}

/**
 * 登录Model
 */
interface LoginModel : Model {
    /**
     * 登录
     */
    fun login(context: Context?, userName: String, pass: String, result: RequestData<UserDto>)
}

/**
 * 登录view
 */
interface LoginView : UiView {
    /**
     * 清除所有的错误信息
     */
    fun clearErrorInfo()

    /**
     * 登录成功
     */
    fun loginSuccess()

    /**
     * 登录失败
     */
    fun loginFailed(error: String)

    /**
     * 显示提示弹窗
     */
    fun showTipDialog(@StringRes info: Int)

    /**
     * 显示错误信息
     */
    fun showErrorInfo(inputType: LoginAty.LoginInputType, @StringRes error: Int)
}
