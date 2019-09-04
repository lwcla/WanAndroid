package com.konsung.cla.demo2.web

import android.content.Context
import com.konsung.basic.config.RequestResult
import com.konsung.basic.ui.BasicPresenter
import com.konsung.basic.ui.BasicView
import com.konsung.basic.util.RequestUtils

class CollectPresenter(private var view: CollectView?) : BasicPresenter() {

    override fun destroy() {
        view = null
    }

    fun collect(context: Context?, id: Int) {

        if (context == null) {
            return
        }

        val result = object : RequestResult<String>(view) {

        }

        RequestUtils.instance.collect(context, id, result)
    }

}


open class CollectView : BasicView<String>()