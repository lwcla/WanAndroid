package com.konsung.basic.db.greendao

import android.content.Context
import com.greendao.dao.DaoMaster
import com.greendao.dao.DaoSession
import com.greendao.dao.UserInfoDao
import com.konsung.basic.bean.UserInfo
import com.konsung.basic.db.DbHelper

class GreenDaoUtil : DbHelper {

    companion object {
        val instance = GreenDaoUtil()
    }

    private var greenDaoManager: GreenDaoManager? = null
    private val daoMaster: DaoMaster? by lazy { greenDaoManager?.daoMaster }
    private val daoSession: DaoSession? by lazy { greenDaoManager?.daoSession }

    override fun init(context: Context) {
        greenDaoManager = GreenDaoManager.getInstance(context)
    }

    override fun saveUserInfo(userInfo: UserInfo) {
        daoSession?.userInfoDao?.insertOrReplace(userInfo)
    }

    override fun getUserInfo(userName: String): UserInfo? {

        val whereCondition = UserInfoDao.Properties.UserName.eq(userName)

        val list = daoSession?.userInfoDao?.queryBuilder()?.where(whereCondition)?.list()
        return if (list?.size ?: 0 == 0) {
            null
        } else {
            list?.get(0)
        }
    }

}