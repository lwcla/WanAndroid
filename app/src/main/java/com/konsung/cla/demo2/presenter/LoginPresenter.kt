package com.konsung.cla.demo2.presenter

import com.konsung.basic.bean.UserDto
import com.konsung.basic.ui.BasePresenter2
import com.konsung.basic.ui.BasicView
import com.konsung.basic.ui.UiView


class LoginPresenter(uiView: UiView?, view: LoginView?) : BasePresenter2<UserDto, LoginView>(uiView, view) {

    fun login(userName: String, pass: String) {
        request { ctx, result ->
            httpHelper.login(ctx, userName, pass, result)
        }
    }
}

open class LoginView : BasicView<UserDto>()