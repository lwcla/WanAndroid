package com.konsung.cla.demo2.login

import android.content.Context
import com.konsung.basic.bean.UserDto
import com.konsung.basic.bean.UserInfo
import com.konsung.basic.db.DbFactory
import com.konsung.basic.db.DbType
import com.konsung.basic.ui.BasePresenter
import com.konsung.basic.ui.BasicView

class RegisterPresenter(view: RegisterView?) : BasePresenter<UserDto, RegisterView>(view) {

    private var userName: String? = null
    private var pass1: String? = null

    override fun success(t: UserDto) {
        //注册成功之后，把账号密码存起来
        val userInfo = UserInfo()
        userInfo.userName = userName
        userInfo.passWord = pass1
        DbFactory.getDb(DbType.GREEN_DAO).saveUserInfo(userInfo)
        super.success(t)
    }

    fun register(context: Context?, userName: String, pass1: String, pass2: String) {

        this.userName = userName
        this.pass1 = pass1

        request(context) { ctx, result ->
            httpHelper.register(ctx, userName, pass1, pass2, result)
        }

    }

}


open class RegisterView : BasicView<UserDto>()