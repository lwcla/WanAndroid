package com.konsung.cla.demo2.main.fragment.home

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.konsung.basic.bean.BannerData
import com.konsung.cla.demo2.App
import com.konsung.cla.demo2.R
import com.youth.banner.listener.OnBannerListener


class BannerHeadView(context: Context) : RelativeLayout(context) {

    companion object {
        val TAG: String = BannerHeadView::class.java.simpleName
    }

    private val mHandler = Handler(Looper.getMainLooper())
    var loadSuccess = false
    private val banner: MyBanner
    private val imvPlace: ImageView
    private var pathList: MutableList<String> = mutableListOf()
    private var titleList: MutableList<String> = mutableListOf()
    private var dataList: MutableList<BannerData> = mutableListOf()

    init {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.view_header_banner, this, true)

        val height = resources.getDimension(R.dimen.dp_200)//获取对应资源文件下的dp值
//        Debug.info(TAG, "BannerHeadView dpValue=$height")
        this.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, height.toInt())
        this.background = ContextCompat.getDrawable(context, android.R.color.white)

        banner = view.findViewById(R.id.banner)
        banner.setOnBannerListener(object : OnBannerListener {
            override fun OnBannerClick(position: Int) {
                if (position >= dataList.size) {
                    return
                }

                val data = dataList[position]

                App.productUtils.startWebAty(context, data.title, data.url, data.id, false)
            }
        })

        imvPlace = view.findViewById(R.id.imvPlace)
        imvPlace.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }

    fun error() {
        loadSuccess = false
        mHandler.post {
            banner.visibility = View.GONE
            imvPlace.visibility = View.VISIBLE
        }
    }

    /**
     * 设置数据
     * @param dataList  数据
     * @param playNow  是否现在就开始轮播
     */
    fun setData(dataList: List<BannerData>, playNow: Boolean) {

        this.dataList.addAll(dataList)

        for (d in dataList) {

            /* if (d.isVisible !=View.VISIBLE){
                 continue
             }*/

            d.imagePath?.let {

                pathList.add(it)
                titleList.add(d.title ?: "")
            }
        }

        if (pathList.size == 0) {
            return
        }

        loadSuccess = true

        mHandler.post {
            if (playNow) {
                banner.init(pathList, titleList)
            }

            banner.visibility = View.VISIBLE
            imvPlace.visibility = View.GONE
        }
    }

    fun startPlay() {

        if (!banner.hasInit) {
            banner.init(pathList, titleList)
            return
        }

        banner.startAutoPlay()
    }

    fun stopAutoPlay() {
        if (!loadSuccess) {
            return
        }

        banner.stopAutoPlay()
    }
}