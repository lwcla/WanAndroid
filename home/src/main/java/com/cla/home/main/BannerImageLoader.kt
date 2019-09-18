package com.cla.home.main

import android.content.Context
import android.widget.ImageView
import com.konsung.basic.config.ImageLoadUtil
import com.youth.banner.loader.ImageLoader

class BannerImageLoader : ImageLoader() {

    override fun displayImage(context: Context?, path: Any?, imageView: ImageView?) {
        ImageLoadUtil.imageLoad.into(context, path.toString(), imageView)
    }

}