package com.konsung.cla.demo2.login

import android.content.Context
import com.konsung.basic.bean.UserDto
import com.konsung.basic.bean.UserInfo
import com.konsung.basic.config.RequestResult
import com.konsung.basic.db.DbFactory
import com.konsung.basic.db.DbType
import com.konsung.basic.ui.BasicPresenter
import com.konsung.basic.ui.BasicView
import com.konsung.basic.util.RequestUtils

class RegisterPresenter(private var view: RegisterView?) : BasicPresenter() {

    override fun destroy() {
        view = null
    }

    fun register(context: Context?, userName: String, pass1: String, pass2: String) {

        if (context == null) {
            return
        }

        val result = object : RequestResult<UserDto>(view) {

            override fun success(t: UserDto) {
                //注册成功之后，把账号密码存起来
                val userInfo = UserInfo()
                userInfo.userName = userName
                userInfo.passWord = pass1
                DbFactory.getDb(DbType.GREEN_DAO).saveUserInfo(userInfo)

                super.success(t)
            }
        }

        RequestUtils.instance.register(context, userName, pass1, pass2, result)
    }

}


open class RegisterView : BasicView<UserDto>()