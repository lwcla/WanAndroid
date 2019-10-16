package com.konsung.basic.db.greendao;

import android.content.Context;

import com.github.yuweiguocn.library.greendao.MigrationHelper;
import com.greendao.dao.DaoMaster;
import com.greendao.dao.UserInfoDao;

import org.greenrobot.greendao.database.Database;

public class DaoOpenHelper extends DaoMaster.OpenHelper {

    public DaoOpenHelper(Context context, String name) {
        super(context, name);
        MigrationHelper.DEBUG = true; //如果查看数据库更新的Log，请设置为true
    }

    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        //把需要管理的数据库表DAO作为最后一个参数传入到方法中
        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {

                    public void onCreateAllTables(Database db, boolean ifNotExists) {
                        DaoMaster.createAllTables(db, ifNotExists);
                    }

                    public void onDropAllTables(Database db, boolean ifExists) {
                        DaoMaster.dropAllTables(db, ifExists);
                    }
                },
                UserInfoDao.class,
                UserInfoDao.class);
    }
}
