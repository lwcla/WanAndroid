package com.konsung.cla.demo2.presenter

import com.konsung.basic.bean.search.SearchKey
import com.konsung.basic.db.DbFactory
import com.konsung.basic.presenter.Presenter
import com.konsung.basic.presenter.UiView

/**
 * 搜索记录presenter实现类
 */
class LoadSearchHistoryPresenterImpl(private var uiView: LoadSearchHistoryView?) : LoadSearchHistoryPresenter {

    private val model: LoadSearchHistoryModel = LoadSearchHistoryModelImpl()

    override fun clear() {
        try {
            model.clear()
            uiView?.clearSearchHistorySuccess()
        } catch (e: Exception) {
            uiView?.clearSearchHistoryFailed(e.message ?: "")
        }
    }

    override fun loadSearchHistory() {
        try {
            val keys = model.loadSearchHistory()
            uiView?.loadSearchHistorySuccess(keys)
        } catch (e: Exception) {
            uiView?.loadSearchHistoryFailed(e.message ?: "")
        }
    }

    override fun saveKey(searchKey: SearchKey) {
        try {
            model.saveKey(searchKey)
            uiView?.saveSearchHistorySuccess()
        } catch (e: Exception) {
            uiView?.saveSearchHistoryFailed(e.message ?: "")
        }
    }

    override fun stop() {

    }

    override fun destroy() {
        uiView = null
    }
}

/**
 * 加载搜索记录model实现类
 */
private class LoadSearchHistoryModelImpl : LoadSearchHistoryModel {

    override fun clear() {
        DbFactory.getDb().clearSearchHistory()
    }

    override fun loadSearchHistory(): List<SearchKey> {
        return DbFactory.getDb().loadSearchKey()
    }

    override fun saveKey(searchKey: SearchKey) {
        DbFactory.getDb().saveSearchKey(searchKey)
    }
}

/**
 * 加载搜索记录Presenter
 */
interface LoadSearchHistoryPresenter : Presenter {

    /**
     * 清除搜索记录
     */
    fun clear()

    /**
     * 加载搜索记录
     */
    fun loadSearchHistory()

    /**
     * 保存搜索记录
     */
    fun saveKey(searchKey: SearchKey)
}

/**
 * 加载搜索记录model
 */
interface LoadSearchHistoryModel {

    fun clear()

    fun loadSearchHistory(): List<SearchKey>

    fun saveKey(searchKey: SearchKey)
}

/**
 * 加载搜索记录view
 */
interface LoadSearchHistoryView : UiView {

    /**
     * 加载记录成功
     */
    fun loadSearchHistorySuccess(list: List<SearchKey>)

    /**
     * 清除记录成功
     */
    fun clearSearchHistorySuccess() {
        //默认空实现
    }

    /**
     * 清除记录失败
     */
    fun clearSearchHistoryFailed(error: String) {
        //默认空实现
    }

    /**
     * 加载记录失败
     */
    fun loadSearchHistoryFailed(error: String) {
        //空实现
    }

    /**
     * 保存记录成功
     */
    fun saveSearchHistorySuccess() {
        //默认空实现
    }

    /**
     * 保存记录失败
     */
    fun saveSearchHistoryFailed(error: String) {
        //默认空实现
    }
}