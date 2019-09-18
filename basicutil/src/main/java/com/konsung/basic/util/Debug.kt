package com.konsung.basic.util

import android.util.Log

class Debug {

    companion object {

        /**
         * 打印日志
         *
         * @param tag tag
         * @param info info
         * @param printStack 是否打印堆栈信息
         */
        fun info(tag: String, info: String, printStack: Boolean = false) {

            if (!AppUtils.isDebug()) {
                return
            }

            Log.i(tag, "lwl  $info")

            if (printStack) {
                for (i in Thread.currentThread().stackTrace) {
                    Log.i(tag, "lwl printStack $i")
                }
            }
        }
    }
}
