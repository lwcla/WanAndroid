package com.konsung.basic.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.InputStream

class FileUtils {

    companion object {

        /** 根据路径获取Bitmap图片
         * @param context
         * @param path
         * @return
         */
        fun getAssetsBitmap(context: Context, path: String): Bitmap {
            val am = context.assets
            var inputStream: InputStream? = null

            try {
                inputStream = am.open(path)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return BitmapFactory.decodeStream(inputStream)
        }
    }

}