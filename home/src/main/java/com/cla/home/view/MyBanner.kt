package com.cla.home.view

import android.content.Context
import android.util.AttributeSet
import com.cla.home.main.BannerImageLoader
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer

class MyBanner(context: Context, attrs: AttributeSet) : Banner(context, attrs) {

    companion object {
        val TAG: String = MyBanner::class.java.simpleName
    }

    var hasInit = false

    fun init(pathList: List<String>, titleList: List<String>) {
        hasInit = true
        //设置banner样式
        setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE)
        //设置图片加载器
        setImageLoader(BannerImageLoader())
        //设置图片集合
        setImages(pathList)
        //设置banner动画效果
        setBannerAnimation(Transformer.Accordion)
        //设置标题集合（当banner样式有显示title时）
        setBannerTitles(titleList)
        //设置自动轮播，默认为true
        isAutoPlay(true)
        //设置轮播时间
        setDelayTime(3000)
        //设置指示器位置（当banner模式中有指示器时）
        setIndicatorGravity(BannerConfig.CENTER)
        start()
    }
}