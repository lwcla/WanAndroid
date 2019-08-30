package com.konsung.basic.db

import android.content.Context
import com.konsung.basic.bean.UserInfo
import com.konsung.basic.db.greendao.GreenDaoUtil

enum class DbType {
    GREEN_DAO
}

class DbFactory {

    companion object {
        fun getDb(dbType: DbType): DbHelper {
            return when (dbType) {
                DbType.GREEN_DAO -> GreenDaoUtil.instance
            }
        }
    }
}

interface DbHelper {

    fun init(context: Context)

    fun saveUserInfo(userInfo: UserInfo)

    fun getUserInfo(userName: String): UserInfo?

}




