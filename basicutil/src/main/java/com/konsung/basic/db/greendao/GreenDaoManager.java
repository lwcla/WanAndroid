package com.konsung.basic.db.greendao;

import android.content.Context;

import com.greendao.dao.DaoMaster;
import com.greendao.dao.DaoSession;
import com.konsung.basic.config.BaseConfig;

/**
 * GreenDaoManager
 */
public class GreenDaoManager {

    private static volatile GreenDaoManager instance = null; //单例

    private DaoMaster daoMaster = null;
    private DaoSession daoSession = null;

    private GreenDaoManager(Context context) {
        // 此处openhelper为自动生成开发所使用，发布版本需自定义
        DaoMaster.OpenHelper devOpenHelper = new DaoOpenHelper(context, BaseConfig.DATABASE_NAME);
        //GreenDaoContext为创建数据库路径使用
        daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        daoSession = daoMaster.newSession();
    }


    /**
     * 单例获取dao管理器
     *
     * @param context 上下文
     * @return dao管理器
     */
    public static GreenDaoManager getInstance(Context context) {
        if (instance == null) {
            synchronized (GreenDaoManager.class) {
                //保证异步处理安全操作
                if (instance == null) {
                    instance = new GreenDaoManager(context);
                }
            }
        }
        return instance;
    }

    public DaoMaster getDaoMaster() {
        return daoMaster;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
