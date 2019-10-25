package com.cla.navigation

import android.content.Context
import com.konsung.basic.bean.navigation.NavigationBean
import com.konsung.basic.presenter.BasicPresenter2
import com.konsung.basic.presenter.BasicView
import com.konsung.basic.presenter.UiView

class LoadNavigation(uiView: UiView?, view: LoadNavigationView?) : BasicPresenter2<List<NavigationBean>, LoadNavigationView>(uiView, view) {

    override fun success(context: Context, t: List<NavigationBean>) {
        val list = mutableListOf<NavigationBean>()
        list.addAll(t.filter {
            !it.name.isNullOrEmpty() && it.articles?.size ?: 0 > 0
        })

        super.success(context, list)
    }

    fun load() {
        request { ctx, result -> httpHelper.fetNavigationList(ctx, result) }
    }

}

open class LoadNavigationView : BasicView<List<NavigationBean>>()