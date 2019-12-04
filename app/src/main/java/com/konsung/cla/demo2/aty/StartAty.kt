package com.konsung.cla.demo2.aty

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.konsung.cla.demo2.App
import com.konsung.cla.demo2.R
import kotlinx.android.synthetic.main.activity_start.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

//TODO 启动逻辑需要修改，这个动画不合适
class StartAty : AppCompatActivity() {

    companion object {
        const val TOTAL_TIME = 2 * 1000L
        const val TIMES = 10
        const val SHOW_ANI_TIME = TOTAL_TIME / TIMES * 5
        const val HIDE_ANI_TIME = TOTAL_TIME / TIMES * 2
    }

    private val timer = Executors.newScheduledThreadPool(1)
    private var future: ScheduledFuture<*>? = null

    private var showAnimator: ObjectAnimator? = null
    private var hideAnimator: ObjectAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        //设置全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        showAnimator = ObjectAnimator.ofInt(imvBg, "imageAlpha", 0, 255)
        showAnimator?.duration = SHOW_ANI_TIME
        showAnimator?.addListener(object : AnimatorListenerAdapter() {

            override fun onAnimationStart(animation: Animator?) {
                imvBg.visibility = View.VISIBLE
                imvBg.imageAlpha = 0
            }
        })
        showAnimator?.start()

        var num = 0
        future = timer.scheduleAtFixedRate({

            if (num == TIMES) {
                runOnUiThread {
                    App.productUtils.startMainAty(this@StartAty)
                    closeStartAty()
                    future?.cancel(true)
                }
            }

            ++num

        }, 0, TOTAL_TIME / TIMES, TimeUnit.MILLISECONDS)
    }

    private fun closeStartAty() {
        thread {
            Thread.sleep((MainActivity.INIT_DELAY * 2.5).toLong())
            println("lwl finish")
            finishAfterTransition()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        showAnimator?.cancel()
        hideAnimator?.cancel()
        future?.cancel(true)
    }
}
