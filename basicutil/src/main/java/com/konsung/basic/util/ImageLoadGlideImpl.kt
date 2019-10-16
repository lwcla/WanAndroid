package com.konsung.basic.util

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.konsung.basic.config.ImageLoad
import com.konsung.basic.config.ImageLoadResult

class ImageLoadGlideImpl private constructor() : ImageLoad {

    companion object {
        val TAG: String = ImageLoadGlideImpl::class.java.simpleName
        val instance by lazy { ImageLoadGlideImpl() }
    }

    override fun into(context: Context?, path: String, imageView: ImageView?, result: ImageLoadResult?) {
        if (context == null || imageView == null) {
            return
        }

        if (result == null) {
            Glide.with(context).load(path).into(imageView)
        } else {
            Glide.with(context)
                    .asBitmap()
                    .load(path)
                    .placeholder(R.mipmap.placeholder)
                    .error(R.mipmap.pic_loading_fail)
                    .listener(object : RequestListener<Bitmap> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                            result.failed(null)
                            return false
                        }

                        override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            result.success()
                            return false
                        }
                    })
                    /* .fitCenter()
                     .centerCrop()*/
                    .into(imageView)
        }
    }

    override fun clearMemory(context: Context?) {
    }

    override fun stop(context: Context?) {
        context?.let {
            Glide.with(it).onStop()
        }
    }

    override fun pauseRequests(context: Context?) {
        context?.let {
            Glide.with(it).pauseRequests()
        }
    }

    override fun resumeRequests(context: Context?) {
        context?.let {
            Glide.with(it).resumeRequests()
        }
    }
}
