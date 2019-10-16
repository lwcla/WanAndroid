package com.konsung.basic.config

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.konsung.basic.util.ImageLoadGlideImpl

interface ImageLoad {

    /**
     * 加载图片
     */
    fun into(context: Context?, path: String, imageView: ImageView?, result: ImageLoadResult? = null)

    /**
     * 停止加载
     */
    fun stop(context: Context?)

    /**
     * 清理存储数据
     */
    fun clearMemory(context: Context?)

    /**
     * 暂停请求
     */
    fun pauseRequests(context: Context?)

    /**
     * 恢复请求
     */
    fun resumeRequests(context: Context?)
}

interface ImageLoadResult {
    fun failed(drawable: Drawable?)

    fun success()
}

class ImageLoadUtil {

    companion object {
        val imageLoad: ImageLoad
            get() = ImageLoadGlideImpl.instance
    }
}



