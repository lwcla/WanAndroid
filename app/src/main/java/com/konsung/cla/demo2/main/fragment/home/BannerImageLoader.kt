package com.konsung.cla.demo2.main.fragment.home

import android.content.Context
import android.widget.ImageView
import com.konsung.basic.util.ImageLoad
import com.youth.banner.loader.ImageLoader

class BannerImageLoader : ImageLoader() {

    override fun displayImage(context: Context?, path: Any?, imageView: ImageView?) {
        ImageLoad.into(context, path.toString(), imageView)
    }

}