package com.konsung.basic.util

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide

class ImageLoad {

    companion object {

        fun into(context: Context?, path: String, imageView: ImageView?) {
            if (context == null || imageView == null) {
                return
            }

            Glide.with(context).load(path).into(imageView)
        }

    }

}