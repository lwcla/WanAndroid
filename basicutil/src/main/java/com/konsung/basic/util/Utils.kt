package com.konsung.basic.util

import android.content.Context
import android.net.ConnectivityManager
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import com.konsung.basic.bean.TwoBean

class Utils {

    companion object {

        private val TAG: String = Utils::class.java.simpleName

        /**
         * 获取屏幕宽高
         */
        fun getAndroiodScreenProperty(context: Context): TwoBean<Int, Int> {
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val dm = DisplayMetrics()
            wm.defaultDisplay.getMetrics(dm)
            val width = dm.widthPixels// 屏幕宽度（像素）
            val height = dm.heightPixels // 屏幕高度（像素）
            val density = dm.density//屏幕密度（0.75 / 1.0 / 1.5）
            val densityDpi = dm.densityDpi//屏幕密度dpi（120 / 160 / 240）
            //屏幕宽度算法:屏幕宽度（像素）/屏幕密度
            val screenWidth = (width / density).toInt()//屏幕宽度(dp)
            val screenHeight = (height / density).toInt()//屏幕高度(dp)
            println("lwl $TAG Utils getAndroiodScreenProperty screenWidth=$screenWidth screenHeight=$screenHeight")
            return TwoBean(screenWidth, screenHeight)
        }

        /**
         * 获取屏幕宽高
         */
        fun getScreenProperty(context: Context): TwoBean<Int, Int> {
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val dm = DisplayMetrics()
            wm.defaultDisplay.getMetrics(dm)
            val width = dm.widthPixels// 屏幕宽度（像素）
            val height = dm.heightPixels // 屏幕高度（像素）
            Debug.info(TAG, "Utils getScreenProperty width=$width height=$height")
            return TwoBean(width, height)
        }


        /**
         * 获取网络状态
         * @param context 上下文
         * @return 网络状态
         */
        fun netConnect(context: Context?): Boolean {
            if (context == null) {
                return false
            }
            val connectivityManager = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnectedOrConnecting
        }

        /**
         * 拿到view的真实高度，即使状态是gone
         * @param view view
         * @return height
         */
        fun getUnDisplayViewHeight(view: View): Int {
            view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
            return view.measuredHeight
        }

        /**
         * 拿到view的真实高度，即使状态是gone
         * @param view view
         * @return height
         */
        fun getUnDisplayViewWidth(view: View): Int {
            view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
            return view.measuredWidth
        }
    }

}