package com.konsung.basic.db.greendao

import android.content.Context

import com.github.yuweiguocn.library.greendao.MigrationHelper
import com.greendao.dao.DaoMaster
import com.greendao.dao.UserInfoDao

import org.greenrobot.greendao.database.Database

/**
 * 用GreenDaoUpgradeHelper实现数据库升级
 */
class DbHelper2(context: Context, name: String) : DaoMaster.OpenHelper(context, name) {

    companion object {

        var update: Boolean? = false
    }

    init {
        MigrationHelper.DEBUG = true //如果查看数据库更新的Log，请设置为true
    }

    override fun onUpgrade(db: Database?, oldVersion: Int, newVersion: Int) {

        //把需要管理的数据库表DAO作为最后一个参数传入到方法中
        MigrationHelper.migrate(db, object : MigrationHelper.ReCreateAllTableListener {
            override fun onCreateAllTables(db: Database, ifNotExists: Boolean) {
                DaoMaster.createAllTables(db, ifNotExists)
            }

            override fun onDropAllTables(db: Database, ifExists: Boolean) {
                DaoMaster.dropAllTables(db, ifExists)
            }
        }, UserInfoDao::class.java)

    }


}
