package com.konsung.cla.demo2.aty

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Color
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.annotation.StringRes
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.konsung.basic.config.BaseConfig
import com.konsung.basic.dialog.BasicDialog
import com.konsung.basic.presenter.LogoutPresenter
import com.konsung.basic.presenter.LogoutPresenterImpl
import com.konsung.basic.presenter.LogoutView
import com.konsung.basic.presenter.Presenter
import com.konsung.basic.ui.BasicAty
import com.konsung.basic.util.FileUtils
import com.konsung.basic.util.SpUtils
import com.konsung.basic.util.Utils
import com.konsung.basic.util.toast
import com.konsung.basic.view.HeightView
import com.konsung.cla.demo2.R
import com.konsung.cla.demo2.presenter.*
import jp.wasabeef.blurry.Blurry
import kotlinx.android.synthetic.main.activity_login.*

/**
 * 登录
 */
class LoginAty : BasicAty(), View.OnClickListener, RegisterView, LoginView, LogoutView {

    companion object {
        val TAG: String = LoginAty::class.java.simpleName
    }

    enum class LoginInputType {
        USER,//账号
        PASS,//密码
        PASS2//确认密码
    }

    private val heightView by lazy { HeightView(tilPassWord2) }
    private val registerPresenter: RegisterPresenter by lazy { RegisterPresenterImpl(this) }
    private val loginPresenter: LoginPresenter by lazy { LoginPresenterImpl(this) }
    private val logoutPresenter: LogoutPresenter by lazy { LogoutPresenterImpl(this) }

    private var animatorSet: AnimatorSet? = null

    override fun getLayoutId(): Int = R.layout.activity_login

    override fun initPresenterList(): List<Presenter>? = listOf(registerPresenter, loginPresenter, logoutPresenter)

    override fun initView() {
        initBg()

        val userName = SpUtils.getString(context, BaseConfig.USER_NAME, "")
        if (userName.isNullOrEmpty()) {
            llLogout.visibility = View.GONE
            llLogin.visibility = View.VISIBLE
        } else {
            tvUserName.text = userName
            llLogout.visibility = View.VISIBLE
            llLogin.visibility = View.GONE
        }

        setSupportActionBar(toolbar)//利用Toolbar代替ActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun initEvent() {
        //设置导航Button点击事件
        toolbar.setNavigationOnClickListener { finish() }
        imvBg.setOnClickListener(this)
        rlRegister.setOnClickListener(this)
        cardViewLogin.setOnClickListener(this)
        cardViewLogout.setOnClickListener(this)
    }

    override fun initData() {

    }

    /**
     * 初始化背景
     */
    private fun initBg() {
        //毛玻璃背景
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
     * 显示错误信息
     */
    private fun showError(tvInput: TextInputLayout, etInput: TextInputEditText, @StringRes errorRes: Int) {
        try {
            tvInput.error = getString(errorRes)
            etInput.requestFocus()
            showKeyboard(etInput)
        } catch (e: Exception) {
        }
    }

    /**
     * 显示注册
     * @param toLoginView 是否切换为登录状态
     */
    private fun showRegister(toLoginView: Boolean) {

        if (toLoginView) {
            toast(TAG, R.string.registered_success)
        }

        if (toLoginView && tilPassWord2.visibility == View.GONE) {
            //已经是登录状态
            return
        }

        if (!toLoginView && tilPassWord2.visibility == View.VISIBLE) {
            //已经是注册状态
            return
        }

        clearErrorInfo()

        if (!toLoginView) {
            //切换到注册状态时，清空确认密码的输入框
            etPassWord2.setText("")
        }

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

        if (toLoginView) {
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
            duration = 200
            interpolator = DecelerateInterpolator()
            start()
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    if (toLoginView) {
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

    override fun clearErrorInfo() {
        tilUser.error = ""
        tilPassWord.error = ""
        tilPassWord2.error = ""
    }

    override fun dismiss(dialog: BasicDialog, clickCancel: Boolean) {
        //弹窗被手动关闭，取消登录
        loginPresenter.stop()
        registerPresenter.stop()
        logoutPresenter.stop()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.imvBg -> hideSoftKeyboard(this)

            R.id.rlRegister -> showRegister(tilPassWord2.visibility != View.GONE)

            R.id.cardViewLogout -> logoutPresenter.logout()

            R.id.cardViewLogin -> {
                val toLogin: Boolean = tilPassWord2.visibility == View.GONE
                if (toLogin) {
                    //去登录
                    loginPresenter.login(etUser.text.toString(), etPassWord.text.toString())
                } else {
                    //去注册
                    registerPresenter.register(etUser.text.toString(), etPassWord.text.toString(), etPassWord2.text.toString())
                }
            }
        }
    }

    override fun loadComplete(success: Boolean) {
        dismissLoadingDialog()
    }

    override fun loginSuccess() {
        toast(TAG, R.string.login_success)
        finishAfterTransition()
    }

    override fun loginFailed(error: String) {
        if (error.contains("账号")) {
            tilUser.error = error
        } else if (error.contains("密码")) {
            tilPassWord.error = error
        }
    }

    override fun logoutSuccess() {
        toast(TAG, R.string.logout_success)
        llLogout.visibility = View.GONE
        llLogin.visibility = View.VISIBLE
    }

    override fun logoutFailed(error: String) {

    }

    override fun registerSuccess() {
        showRegister(true)
    }

    override fun registerFailed(error: String) {

    }

    override fun showErrorInfo(inputType: LoginInputType, error: Int) {
        when (inputType) {
            LoginInputType.USER -> showError(tilUser, etUser, error)
            LoginInputType.PASS -> showError(tilPassWord, etPassWord, error)
            LoginInputType.PASS2 -> showError(tilPassWord2, etPassWord2, error)
        }
    }

    override fun showTipDialog(info: Int) {
        showLoadingDialog(info, false)
    }
}
