package com.konsung.cla.demo2.login

import android.content.Context
import com.konsung.basic.bean.UserDto
import com.konsung.basic.config.RequestResult
import com.konsung.basic.ui.BasicPresenter
import com.konsung.basic.ui.BasicView
import com.konsung.basic.util.RequestUtils


class LoginPresenter(private var view: LoginView?) : BasicPresenter() {

    private val result = object : RequestResult<UserDto>(view) {

    }

    override fun destroy() {
        view = null
    }

    override fun stop() {
        result.stop = true
    }

    fun login(context: Context?, userName: String, pass: String) {

        if (context == null) {
            return
        }

        result.stop = false
        RequestUtils.instance.login(context, userName, pass, result)
    }
}

open class LoginView : BasicView<UserDto>()