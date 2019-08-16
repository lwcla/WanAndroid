package com.konsung.cla.demo2.util

import android.content.Context
import com.konsung.basic.config.BasicConfig
import com.konsung.cla.demo2.App
import com.konsung.cla.demo2.BuildConfig

class BasicConfigImpl private constructor() : BasicConfig {


    companion object {
        val instance by lazy { BasicConfigImpl() }
    }

    override fun debug(): Boolean {
        return BuildConfig.DEBUG
    }

    override fun getContext(): Context = App.context
}