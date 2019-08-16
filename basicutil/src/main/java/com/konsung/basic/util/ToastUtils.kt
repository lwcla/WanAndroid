package com.konsung.basic.util

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.annotation.StringRes

class ToastUtils private constructor() {


    companion object {
        val instance = ToastUtils()
    }

    private val handler = Handler(Looper.getMainLooper())
    private val map: MutableMap<String, Toast> = mutableMapOf()

    /**
     * toast
     * @param context context
     * @param tag tag
     * @param toastRes 内容
     */
    @SuppressLint("ShowToast")
    fun toast(context: Context?, tag: String, @StringRes toastRes: Int) {

        if (context == null) {
            return
        }

        try {
            toast(context, tag, context.getString(toastRes))
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * toast
     * @param context context
     * @param tag tag
     * @param text text
     */
    @SuppressLint("ShowToast")
    fun toast(context: Context, tag: String, text: String) {

        handler.post {
            var toast: Toast? = null

            if (map.containsKey(tag)) {
                toast = map[tag]
                toast?.apply { setText(text) }
            }

            if (toast == null) {
                toast = Toast.makeText(context, text, Toast.LENGTH_SHORT)
                map[tag] = toast
            }

            toast?.show()
        }
    }

    /**
     * 销毁toast
     * @param tag tag
     */
    fun destroy(tag: String) {

        if (!map.containsKey(tag)) {
            return
        }

        handler.post {
            val toast = map.get(tag)
            toast?.apply { cancel() }
            map.remove(tag)
        }
    }

}