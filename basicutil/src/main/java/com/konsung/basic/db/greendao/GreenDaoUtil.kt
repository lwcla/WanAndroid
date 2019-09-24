package com.konsung.basic.db.greendao

import android.content.Context
import com.greendao.dao.DaoMaster
import com.greendao.dao.DaoSession
import com.greendao.dao.SearchKeyDao
import com.greendao.dao.UserInfoDao
import com.konsung.basic.bean.UserInfo
import com.konsung.basic.bean.search.SearchKey
import com.konsung.basic.db.DbHelper
import com.konsung.basic.util.AppUtils
import java.util.*

class GreenDaoUtil : DbHelper {

    companion object {
        val instance = Single.INSTANCE
    }

    init {
        init(AppUtils.getContext())
    }

    private var greenDaoManager: GreenDaoManager? = null
    private val daoMaster: DaoMaster? by lazy { greenDaoManager?.daoMaster }
    private val daoSession: DaoSession? by lazy { greenDaoManager?.daoSession }

    override fun init(context: Context) {
        greenDaoManager = GreenDaoManager.getInstance(context)
    }

    override fun clearSearchHistory() {
        daoSession?.searchKeyDao?.deleteAll()
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

    override fun saveSearchKey(searchKey: SearchKey) {

        val whereCondition = SearchKeyDao.Properties.Name.eq(searchKey.name)
        val list = daoSession?.searchKeyDao?.queryBuilder()?.where(whereCondition)?.list()
        if (list?.size ?: 0 > 0) {
            for (key in list!!) {
                daoSession?.searchKeyDao?.delete(key)
            }
        }

        searchKey.saveTime = Date()
        daoSession?.searchKeyDao?.insertOrReplace(searchKey)
    }

    override fun loadSearchKey(): List<SearchKey> {
        val list = daoSession?.searchKeyDao?.queryBuilder()?.orderDesc(SearchKeyDao.Properties.SaveTime)?.list()
        return if (list?.size ?: 0 == 0) {
            emptyList()
        } else {
            list!!
        }
    }

    class Single {
        companion object {
            val INSTANCE = GreenDaoUtil()
        }
    }
}