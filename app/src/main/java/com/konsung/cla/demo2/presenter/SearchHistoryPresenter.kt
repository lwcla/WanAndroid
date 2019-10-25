package com.konsung.cla.demo2.presenter

import com.konsung.basic.bean.search.SearchKey
import com.konsung.basic.db.DbFactory
import com.konsung.basic.presenter.BasicPresenter1
import com.konsung.basic.presenter.BasicView
import com.konsung.basic.presenter.UiView

class SearchHistoryPresenter(uiView: UiView?, view: SearchHistoryView?) : BasicPresenter1<List<SearchKey>, SearchHistoryView>(uiView, view) {

    fun clear() {
        DbFactory.getDb().clearSearchHistory()
        view?.success(listOf(), true)
    }

    fun load() {
        val keys = DbFactory.getDb().loadSearchKey()
        view?.success(keys, true)
    }

    fun save(searchKey: SearchKey) {

        if (view == null) {
            return
        }

        DbFactory.getDb().saveSearchKey(searchKey)
    }
}

open class SearchHistoryView : BasicView<List<SearchKey>>()