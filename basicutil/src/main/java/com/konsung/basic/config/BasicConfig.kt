package com.konsung.basic.config

import android.content.Context

interface BasicConfig {

    fun debug(): Boolean

    fun getContext(): Context

}