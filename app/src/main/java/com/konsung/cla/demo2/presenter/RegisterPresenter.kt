package com.konsung.cla.demo2.presenter

import android.content.Context
import androidx.annotation.StringRes
import com.konsung.basic.bean.UserDto
import com.konsung.basic.bean.UserInfo
import com.konsung.basic.config.RequestData
import com.konsung.basic.db.DbFactory
import com.konsung.basic.model.BaseModel
import com.konsung.basic.model.Model
import com.konsung.basic.presenter.BasePresenter1
import com.konsung.basic.presenter.Presenter
import com.konsung.basic.presenter.UiView
import com.konsung.cla.demo2.R
import com.konsung.cla.demo2.aty.LoginAty

/**
 * 注册Presenter实现类
 */
class RegisterPresenterImpl(view: RegisterView) : BasePresenter1<UserDto, RegisterView, RegisterModel>(view, RegisterModelImpl()), RegisterPresenter {

    override fun success(t: UserDto, refreshData: Boolean) {
        val userInfo = UserInfo()
        userInfo.userName = t.username
        userInfo.passWord = t.password
        DbFactory.getDb().saveUserInfo(userInfo)
        getUiView()?.registerSuccess()
    }

    override fun failed(message: String, refreshData: Boolean) {
        getUiView()?.registerFailed(message)
    }

    override fun register(userName: String, pass1: String, pass2: String) {

        getUiView()?.clearErrorInfo()

        if (userName.isEmpty()) {
            getUiView()?.showErrorInfo(LoginAty.LoginInputType.USER, R.string.account_can_not_be_empty)
            return
        }

        if (pass1.isEmpty()) {
            getUiView()?.showErrorInfo(LoginAty.LoginInputType.PASS, R.string.pass_word_can_not_be_empty)
            return
        }

        if (pass2.isEmpty()) {
            getUiView()?.showErrorInfo(LoginAty.LoginInputType.PASS2, R.string.pass_word2_can_not_be_empty)
            return
        }

        if (pass1 != pass2) {
            getUiView()?.showErrorInfo(LoginAty.LoginInputType.PASS2, R.string.inconsistent_password_input)
            return
        }


        request { ctx, _, result ->
            getUiView()?.showTipDialog(R.string.register_please_wait)
            model.register(ctx, userName, pass1, pass2, result)
        }
    }
}

/**
 * 注册model实现类
 */
class RegisterModelImpl : BaseModel<UserDto>(), RegisterModel {

    override fun register(context: Context?, userName: String, pass1: String, pass2: String, result: RequestData<UserDto>) {
        context?.let {
            request(it, result) { ctx, requestData ->
                httpHelper.register(ctx, userName, pass1, pass2, requestData)
            }
        }
    }
}

/**
 * 注册Model
 */
interface RegisterModel : Model {
    fun register(context: Context?, userName: String, pass1: String, pass2: String, result: RequestData<UserDto>)
}

/**
 * 注册presenter
 */
interface RegisterPresenter : Presenter {
    fun register(userName: String, pass1: String, pass2: String)
}

/**
 * 注册view
 */
interface RegisterView : UiView {
    /**
     * 清除所有的错误信息
     */
    fun clearErrorInfo()

    /**
     * 注册成功
     */
    fun registerSuccess()

    /**
     * 注册失败
     */
    fun registerFailed(error: String)

    /**
     * 显示提示弹窗
     */
    fun showTipDialog(@StringRes info: Int)

    /**
     * 显示错误信息
     */
    fun showErrorInfo(inputType: LoginAty.LoginInputType, @StringRes error: Int)
}