package com.konsung.basic.db.greendao;

import android.content.Context;

import com.greendao.dao.DaoMaster;
import com.greendao.dao.DaoSession;
import com.greendao.dao.SearchKeyDao;
import com.greendao.dao.UserInfoDao;
import com.konsung.basic.bean.UserInfo;
import com.konsung.basic.bean.search.SearchKey;
import com.konsung.basic.db.DbHelper;
import com.konsung.basic.util.AppUtils;

import org.greenrobot.greendao.query.WhereCondition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GreenDaoUtil1 implements DbHelper {

    private GreenDaoManager1 greenDaoManager = GreenDaoManager1.getInstance(AppUtils.Companion.getContext());
    private DaoMaster daoMaster = greenDaoManager.getDaoMaster();
    private DaoSession daoSession = greenDaoManager.getDaoSession();

    private GreenDaoUtil1() {
    }

    public static GreenDaoUtil1 getInstance() {
        return Single.instance;
    }

    @Override
    public void init(@NotNull Context context) {

    }

    @Override
    public void saveUserInfo(@NotNull UserInfo userInfo) {
        if (daoSession == null) {
            return;
        }

        UserInfoDao dao = daoSession.getUserInfoDao();
        if (dao == null) {
            return;
        }

        dao.insertOrReplace(userInfo);
    }

    @Nullable
    @Override
    public UserInfo getUserInfo(@NotNull String userName) {

        if (daoSession == null) {
            return null;
        }

        UserInfoDao dao = daoSession.getUserInfoDao();
        if (dao == null) {
            return null;
        }

        WhereCondition whereCondition = UserInfoDao.Properties.UserName.eq(userName);

        List<UserInfo> list = dao.queryBuilder().where(whereCondition).list();
        if (list == null || list.size() == 0) {
            return null;
        } else {
            return list.get(0);
        }
    }

    @Override
    public void saveSearchKey(@NotNull SearchKey searchKey) {

        if (daoSession == null) {
            return;
        }

        SearchKeyDao dao = daoSession.getSearchKeyDao();
        if (dao == null) {
            return;
        }

        WhereCondition whereCondition = SearchKeyDao.Properties.Name.eq(searchKey.getName());
        List<SearchKey> list = dao.queryBuilder().where(whereCondition).list();
        if (list != null && list.size() > 0) {
            for (SearchKey key : list) {
                dao.delete(key);
            }
        }

        searchKey.setSaveTime(new Date());
        dao.insertOrReplace(searchKey);
    }

    @NotNull
    @Override
    public List<SearchKey> loadSearchKey() {

        if (daoSession == null) {
            return new ArrayList<>();
        }

        SearchKeyDao dao = daoSession.getSearchKeyDao();
        if (dao == null) {
            return new ArrayList<>();
        }

        List<SearchKey> list = dao.queryBuilder().orderDesc(SearchKeyDao.Properties.SaveTime).list();

        if (list == null || list.size() == 0) {
            return new ArrayList<>();
        } else {
            return list;
        }
    }

    @Override
    public void clearSearchHistory() {
        if (daoSession == null) {
            return;
        }

        SearchKeyDao dao = daoSession.getSearchKeyDao();
        if (dao == null) {
            return;
        }

        dao.deleteAll();
    }

    private static class Single {
        private static GreenDaoUtil1 instance = new GreenDaoUtil1();
    }
}
