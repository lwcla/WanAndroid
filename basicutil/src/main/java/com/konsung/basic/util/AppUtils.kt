package com.konsung.basic.util

import android.content.Context
import com.konsung.basic.config.BasicConfig

class AppUtils {

    companion object {
        val instance = AppUtils()
    }

    private var basicConfig: BasicConfig? = null

    fun init(basicConfig: BasicConfig) {
        this.basicConfig = basicConfig
    }

    fun getBasicConfig() = basicConfig

    fun isDebug(): Boolean {
        return basicConfig?.debug() ?: true
    }

    fun getContext(): Context? {
        return basicConfig?.getContext()
    }

}