package com.konsung.basic.config

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.konsung.basic.util.ImageLoadGlideImpl

interface ImageLoad {

    fun into(context: Context?, path: String, imageView: ImageView?, result: ImageLoadResult? = null)

    fun stop(context: Context?)

    fun clearMemory(context: Context?)
}

interface ImageLoadResult {
    fun failed(drawable: Drawable?)

    fun success()
}

enum class ImageLoadType {
    GLIDE
}

class ImageLoadFactory {

    companion object {

        fun getImageLoad(type: ImageLoadType): ImageLoad {
            return when (type) {
                ImageLoadType.GLIDE -> ImageLoadGlideImpl.instance
            }
        }
    }
}

class ImageLoadUtil {

    companion object {
        val imageLoad: ImageLoad
            get() = ImageLoadFactory.getImageLoad(ImageLoadType.GLIDE)
    }
}



