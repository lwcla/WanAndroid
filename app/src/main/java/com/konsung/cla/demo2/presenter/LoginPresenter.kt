package com.konsung.cla.demo2.presenter

import android.content.Context
import com.konsung.basic.bean.UserDto
import com.konsung.basic.config.BaseConfig
import com.konsung.basic.config.RequestData
import com.konsung.basic.model.BaseModel
import com.konsung.basic.model.Model
import com.konsung.basic.presenter.BasePresenter1
import com.konsung.basic.presenter.Presenter
import com.konsung.basic.presenter.UiView
import com.konsung.basic.util.SpUtils

/**
 * 登录presenter实现类
 */
class LoginPresenterImpl(view: LoginView) : BasePresenter1<UserDto, LoginView, LoginModel>(view, LoginModelImpl()), LoginPresenter {

    override fun success(t: UserDto, refreshData: Boolean) {
        getContext()?.let {
            SpUtils.putString(it, BaseConfig.USER_NAME, t.username)
            getUiView()?.loginResult(true, "")
        }
    }

    override fun failed(message: String, refreshData: Boolean) {
        getUiView()?.loginResult(false, message)
    }

    override fun login(userName: String, pass: String) {
        request { ctx, _, result ->
            model.login(ctx, userName, pass, result)
        }
    }
}

/**
 * 登录Model实现类
 */
class LoginModelImpl : BaseModel<UserDto>(), LoginModel {

    override fun login(context: Context, userName: String, pass: String, result: RequestData<UserDto>) {
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
     * 弹窗被手动关闭，取消登录
     */
    fun stop()

    /**
     * 登录
     */
    fun login(userName: String, pass: String)
}

/**
 * 登录Model
 */
interface LoginModel : Model {
    fun login(context: Context, userName: String, pass: String, result: RequestData<UserDto>)
}

/**
 * 登录view
 */
interface LoginView : UiView {

    /**
     * 登录结果
     * @param success 是否成功
     * @param errorInfo 错误信息
     */
    fun loginResult(success: Boolean, errorInfo: String)
}
