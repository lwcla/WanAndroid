package com.konsung.cla.demo2.login

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Color
import android.view.View
import android.view.animation.LinearInterpolator
import com.konsung.basic.ui.BasicAty
import com.konsung.basic.ui.BasicPresenter
import com.konsung.basic.ui.HeightView
import com.konsung.basic.util.FileUtils
import com.konsung.basic.util.Utils
import com.konsung.cla.demo2.R
import jp.wasabeef.blurry.Blurry
import kotlinx.android.synthetic.main.activity_login.*


class LoginAty : BasicAty(), View.OnClickListener {

    companion object {
        val TAG = LoginAty::class.java.simpleName
    }

    private val heightView by lazy { HeightView(tilPassWord2) }
    private var animatorSet: AnimatorSet? = null

    override fun getLayoutId(): Int = R.layout.activity_login

    override fun initView() {
        initBg()
        setSupportActionBar(toolbar)//利用Toolbar代替ActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun initEvent() {
        //设置导航Button点击事件
        toolbar.setNavigationOnClickListener { finish() }
        imvBg.setOnClickListener(this)
        rlRegister.setOnClickListener(this)
        cardViewLogin.setOnClickListener(this)
    }

    override fun initData() {

    }

    override fun initPresenter(): List<BasicPresenter>? = null

    /**
     * 初始化背景
     */
    private fun initBg() {
        val bitmap = FileUtils.getAssetsBitmap(this, "timg.jpg")
        Blurry.with(context)
                .radius(10)//模糊半径
                .sampling(8)//缩放大小，先缩小再放大
                .color(Color.argb(60, 255, 255, 255))//颜色
                .async()//是否异步
                .from(bitmap)//传入View
                .into(imvBg)//显示View
    }

    /**
     * 登录
     */
    private fun login() {

    }

    /**
     * 注册
     */
    private fun register() {

    }

    /**
     * 显示注册
     */
    private fun showRegister() {

        //是否切换为登录状态
        val login: Boolean = tilPassWord2.visibility != View.GONE

        animatorSet?.cancel()
        tilPassWord2.visibility = View.VISIBLE
        btnLogin.visibility = View.VISIBLE
        btnRegister.visibility = View.VISIBLE
        tvLogin.visibility = View.VISIBLE
        tvRegister.visibility = View.VISIBLE

        val height = Utils.getUnDisplayViewHeight(tilPassWord2)

        val inputHeight1: Int
        val inputHeight2: Int

        val loginHeight1: Float
        val loginHeight2: Float

        val registerHeight1: Float
        val registerHeight2: Float

        if (login) {
            inputHeight1 = height
            inputHeight2 = 0

            loginHeight1 = -height.toFloat() / 2
            loginHeight2 = 0f

            registerHeight1 = 0f
            registerHeight2 = height.toFloat() / 2
        } else {
            inputHeight1 = 0
            inputHeight2 = height

            loginHeight1 = 0f
            loginHeight2 = -height.toFloat() / 2

            registerHeight1 = height.toFloat() / 2
            registerHeight2 = 0f
        }

        val hAnimator = ObjectAnimator.ofInt(heightView, "viewHeight", inputHeight1, inputHeight2)

        val loginAnimator = ObjectAnimator.ofFloat(btnLogin, "translationY", loginHeight1, loginHeight2)
        loginAnimator.duration = 0
        val registerAnimator = ObjectAnimator.ofFloat(btnRegister, "translationY", registerHeight1, registerHeight2)
        registerAnimator.duration = 0

        val tvLoginAnimator = ObjectAnimator.ofFloat(tvLogin, "translationY", registerHeight1, registerHeight2)
        tvLoginAnimator.duration = 0
        val tvRegisterAnimator = ObjectAnimator.ofFloat(tvRegister, "translationY", loginHeight1, loginHeight2)
        tvRegisterAnimator.duration = 0

        animatorSet = AnimatorSet()
        animatorSet?.play(hAnimator)?.with(loginAnimator)?.with(registerAnimator)?.with(tvLoginAnimator)?.with(tvRegisterAnimator)

        animatorSet?.apply {
            duration = 300
            interpolator = LinearInterpolator()
            start()
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    if (login) {
                        tilPassWord2.visibility = View.GONE
                        btnRegister.visibility = View.GONE
                        tvLogin.visibility = View.GONE

                        toolbar.setTitle(R.string.login)
                    } else {
                        btnLogin.visibility = View.GONE
                        tvRegister.visibility = View.GONE

                        toolbar.setTitle(R.string.registered)
                    }
                }
            })
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.imvBg -> hideSoftKeyboard(this)

            R.id.rlRegister -> showRegister()

            R.id.cardViewLogin -> {
                val login: Boolean = tilPassWord2.visibility == View.GONE
                if (login) {
                    //去登录
                    login()
                } else {
                    //去注册
                    register()
                }
            }
        }
    }
}
