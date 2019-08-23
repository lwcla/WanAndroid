package com.konsung.cla.demo2.main.fragment.home

import android.content.Context
import android.widget.ImageView
import com.konsung.basic.config.ImageLoadFactory
import com.konsung.basic.config.ImageLoadType
import com.konsung.basic.config.ImageLoadUtil
import com.youth.banner.loader.ImageLoader

class BannerImageLoader : ImageLoader() {

    override fun displayImage(context: Context?, path: Any?, imageView: ImageView?) {
        ImageLoadUtil.imageLoad.into(context, path.toString(), imageView)
    }

}