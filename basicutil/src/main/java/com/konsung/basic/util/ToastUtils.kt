package com.konsung.basic.util

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

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
            val toast = map[tag]
            toast?.apply { cancel() }
            map.remove(tag)
        }
    }
}

fun Context?.toast(tag: String, @StringRes textRes: Int) {
    this?.let {
        ToastUtils.instance.toast(this, tag, textRes)
    }
}

fun Context?.toast(tag: String, text: String) {
    this?.let {
        ToastUtils.instance.toast(this, tag, text)
    }
}

fun Fragment?.toast(tag: String, text: String) {
    this?.let {
        context.toast(tag, text)
    }
}

fun Fragment?.toast(tag: String, @StringRes textRes: Int) {
    this?.let {
        context.toast(tag, textRes)
    }
}


fun AppCompatActivity?.toast(tag: String, text: String) {
    this?.let {
        ToastUtils.instance.toast(it, tag, text)
    }
}

fun AppCompatActivity?.toast(tag: String, @StringRes textRes: Int) {
    this?.let {
        ToastUtils.instance.toast(it, tag, textRes)
    }
}