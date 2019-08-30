package com.konsung.basic.db.greendao

import android.content.Context

import com.greendao.dao.DaoMaster
import com.greendao.dao.DaoSession
import com.konsung.basic.config.Config

/**
 * 类功能：数据库管理类
 */

class GreenDaoManager
/**
 * dao管理器构造器
 * @param context 上下文
 */
private constructor(context: Context) {

    var daoMaster: DaoMaster? = null
        private set

    var daoSession: DaoSession? = null
        private set

    init {
        if (instance == null) {
            // 此处openhelper为自动生成开发所使用，发布版本需自定义
            val devOpenHelper = DbHelper2(context, Config.DATABASE_NAME)
            //GreenDaoContext为创建数据库路径使用
            daoMaster = DaoMaster(devOpenHelper.writableDatabase)
            daoSession = daoMaster!!.newSession()
        }
    }

    companion object {

        @Volatile
        private var instance: GreenDaoManager? = null //单例

        /**
         * 单例获取dao管理器
         * @param context 上下文
         * @return dao管理器
         */
        fun getInstance(context: Context): GreenDaoManager? {
            if (instance == null) {
                synchronized(GreenDaoManager::class.java) {
                    //保证异步处理安全操作
                    if (instance == null) {
                        instance = GreenDaoManager(context)
                    }
                }
            }
            return instance
        }
    }
}

