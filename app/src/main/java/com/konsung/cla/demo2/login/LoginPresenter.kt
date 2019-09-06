package com.konsung.cla.demo2.login

import android.content.Context
import com.konsung.basic.bean.UserDto
import com.konsung.basic.ui.BasePresenter
import com.konsung.basic.ui.BasicView


class LoginPresenter(context: Context?, view: LoginView?) : BasePresenter<UserDto, LoginView>(context, view) {

    fun login(userName: String, pass: String) {
        request { ctx, result ->
            httpHelper.login(ctx, userName, pass, result)
        }
    }
}

open class LoginView : BasicView<UserDto>()