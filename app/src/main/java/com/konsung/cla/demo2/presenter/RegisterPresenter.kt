package com.konsung.cla.demo2.presenter

import android.content.Context
import com.konsung.basic.bean.UserDto
import com.konsung.basic.bean.UserInfo
import com.konsung.basic.config.RequestData
import com.konsung.basic.db.DbFactory
import com.konsung.basic.model.BaseModel
import com.konsung.basic.model.Model
import com.konsung.basic.presenter.BasePresenter1
import com.konsung.basic.presenter.Presenter
import com.konsung.basic.presenter.UiView

/**
 * 注册Presenter实现类
 */
class RegisterPresenterImpl(view: RegisterView) : BasePresenter1<UserDto, RegisterView, RegisterModel>(view, RegisterModelImpl()), RegisterPresenter {

    override fun success(t: UserDto, refreshData: Boolean) {
        val userInfo = UserInfo()
        userInfo.userName = t.username
        userInfo.passWord = t.password
        DbFactory.getDb().saveUserInfo(userInfo)
        getUiView()?.showRegister(true)
    }

    override fun register(userName: String, pass1: String, pass2: String) {
        request { ctx, _, result ->
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
     * 显示注册
     * @param toLoginView 是否切换为登录状态
     */
    fun showRegister(toLoginView: Boolean)
}