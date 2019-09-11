package com.konsung.basic.util

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.text.Html
import android.text.TextUtils
import android.widget.TextView
import java.util.*


class StringUtils {

    companion object {
        val TAG: String = StringUtils::class.java.simpleName
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

    fun loadTextIcon(context: Context?, textView: TextView) {
        context?.let {
            val iconFont = Typeface.createFromAsset(it.assets, "iconfont.ttf")
            textView.typeface = iconFont
        }
    }

    fun formHtml(string: String?): String {

        val text = clearNull(string?.trim())
        if (text.isEmpty()) {
            return ""
        }

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT).toString()
        } else {
            Html.fromHtml(text).toString()
        }
    }

    //获取随机rgb颜色值
    fun intrandomColor(): Int {
        val random = Random()
        val red = random.nextInt(150);//0-190        ,如果颜色值过大,就越接近白色,就看不清了,所以需要限定范围
        val green = random.nextInt(150);//0-190
        val blue = random.nextInt(150);//0-190
        return Color.rgb(red, green, blue);//使用rgb混合生成一种新的颜色,Color.rgb生成的是一个int数
    }
}