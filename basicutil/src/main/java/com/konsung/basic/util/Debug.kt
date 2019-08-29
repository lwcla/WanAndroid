package com.konsung.basic.util

import android.util.Log

class Debug {

    companion object {


        fun info(tag: String, info: String) {

            if (!AppUtils.isDebug()) {
                return
            }

            Log.i(tag, "lwl  $info")
        }
    }
}
