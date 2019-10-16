package com.konsung.cla.demo2.presenter

import android.content.Context
import com.konsung.basic.bean.UserDto
import com.konsung.basic.bean.UserInfo
import com.konsung.basic.db.DbFactory
import com.konsung.basic.ui.BasePresenter2
import com.konsung.basic.ui.BasicView
import com.konsung.basic.ui.UiView

class RegisterPresenter(uiView: UiView?, view: RegisterView?) : BasePresenter2<UserDto, RegisterView>(uiView, view) {

    private var userName: String? = null
    private var pass1: String? = null

    override fun success(context: Context, t: UserDto) {
        //注册成功之后，把账号密码存起来
        val userInfo = UserInfo()
        userInfo.userName = userName
        userInfo.passWord = pass1
        DbFactory.getDb().saveUserInfo(userInfo)
        super.success(context, t)
    }

    fun register(userName: String, pass1: String, pass2: String) {

        this.userName = userName
        this.pass1 = pass1

        request { ctx, result ->
            httpHelper.register(ctx, userName, pass1, pass2, result)
        }

    }

}


open class RegisterView : BasicView<UserDto>()