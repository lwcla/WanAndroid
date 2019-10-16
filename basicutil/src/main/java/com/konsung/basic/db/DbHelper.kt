package com.konsung.basic.db

import android.content.Context
import com.konsung.basic.bean.UserInfo
import com.konsung.basic.bean.search.SearchKey
import com.konsung.basic.db.greendao.GreenDaoUtil

class DbFactory {
    companion object {
        fun getDb(): DbHelper = GreenDaoUtil.getInstance()
    }
}

interface DbHelper {

    fun init(context: Context)

    fun saveUserInfo(userInfo: UserInfo)

    fun getUserInfo(userName: String): UserInfo?

    fun saveSearchKey(searchKey: SearchKey)

    fun loadSearchKey(): List<SearchKey>

    fun clearSearchHistory()
}




