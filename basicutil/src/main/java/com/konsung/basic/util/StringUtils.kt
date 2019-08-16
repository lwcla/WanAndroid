package com.konsung.basic.util

import android.content.Context
import android.graphics.Typeface
import android.text.TextUtils
import android.widget.TextView


class StringUtils {

    companion object {
        val instance by lazy { StringUtils() }
    }

    fun clearNull(string: String?): String {

        if (string == null) {
            return ""
        }

        if (TextUtils.isEmpty(string)) {
            return ""
        }

        if (TextUtils.equals(string.toUpperCase(), "null".toUpperCase())) {
            return ""
        }

        return string
    }

    fun loadTextIcon(context: Context, textView: TextView) {
        val iconFont = Typeface.createFromAsset(context.assets, "iconfont.ttf")
        textView.typeface = iconFont
    }
}