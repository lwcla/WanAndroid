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
import com.konsung.basic.bean.UserDto
import com.konsung.basic.config.BaseConfig
import com.konsung.basic.dialog.BasicDialog
import com.konsung.basic.presenter.LogoutPresenter
import com.konsung.basic.presenter.LogoutView
import com.konsung.basic.ui.BasicAty
import com.konsung.basic.ui.BasicPresenter
import com.konsung.basic.ui.HeightView
import com.konsung.basic.util.FileUtils
import com.konsung.basic.util.SpUtils
import com.konsung.basic.util.Utils
import com.konsung.basic.util.toast
import com.konsung.cla.demo2.R
import com.konsung.cla.demo2.presenter.LoginPresenter
import com.konsung.cla.demo2.presenter.LoginView
import com.konsung.cla.demo2.presenter.RegisterPresenter
import com.konsung.cla.demo2.presenter.RegisterView
import jp.wasabeef.blurry.Blurry
import kotlinx.android.synthetic.main.activity_login.*

/**
 * 登录
 */
class LoginAty : BasicAty(), View.OnClickListener {

    companion object {
        val TAG: String = LoginAty::class.java.simpleName
    }

    private val heightView by lazy { HeightView(tilPassWord2) }
    private val registerPresenter by lazy { initRegisterPresenter() }
    private val loginPresenter by lazy { initLoginPresenter() }
    private val logoutPresenter by lazy { initLogoutPresenter() }

    private var animatorSet: AnimatorSet? = null

    override fun getLayoutId(): Int = R.layout.activity_login

    override fun initPresenter(): List<BasicPresenter>? {
        return listOf(registerPresenter, loginPresenter, logoutPresenter)
    }

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
     * 登录
     */
    private fun login() {
//        hideSoftKeyboard(this)
        resetEditStatus()

        val userName = etUser.text.toString()
        if (userName.isEmpty()) {
            showError(tilUser, etUser, R.string.account_can_not_be_empty)
            return
        }

        val pass = etPassWord.text.toString()
        if (pass.isEmpty()) {
            showError(tilPassWord, etPassWord, R.string.pass_word_can_not_be_empty)
            return
        }

        showLoadingDialog(R.string.login_please_wait, false)
        loginPresenter.login(userName, pass)
    }

    private fun resetEditStatus() {
        tilUser.error = ""
        tilPassWord.error = ""
        tilPassWord2.error = ""
    }

    private fun showError(tvInput: TextInputLayout, etInput: TextInputEditText, @StringRes errorRes: Int) {
        tvInput.error = getString(errorRes)
        etInput.requestFocus()
        showKeyboard(etInput)
    }

    /**
     * 注册
     */
    private fun register() {
        resetEditStatus()

        val userName = etUser.text.toString()
        if (userName.isEmpty()) {
            showError(tilUser, etUser, R.string.account_can_not_be_empty)
            return
        }

        val pass1 = etPassWord.text.toString()
        if (pass1.isEmpty()) {
            showError(tilPassWord, etPassWord, R.string.pass_word_can_not_be_empty)
            return
        }

        val pass2 = etPassWord2.text.toString()
        if (pass2.isEmpty()) {
            showError(tilPassWord2, etPassWord2, R.string.pass_word2_can_not_be_empty)
            return
        }

        if (pass1 != pass2) {
            showError(tilPassWord2, etPassWord2, R.string.inconsistent_password_input)
            return
        }

        registerPresenter.register(userName, pass1, pass2)
    }

    /**
     * 显示注册
     * @param login 是否切换为登录状态
     */
    private fun showRegister(login: Boolean) {

        if (login && tilPassWord2.visibility == View.GONE) {
            //已经是登录状态
            return
        }

        if (!login && tilPassWord2.visibility == View.VISIBLE) {
            //已经是注册状态
            return
        }

        resetEditStatus()

        if (!login) {
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
            duration = 200
            interpolator = DecelerateInterpolator()
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

    private fun initRegisterPresenter(): RegisterPresenter {

        val view = object : RegisterView() {

            override fun success(t: UserDto, refreshData: Boolean) {
                toast(TAG, R.string.registered_success)
                showRegister(true)
            }
        }

        return RegisterPresenter(this, view)
    }

    private fun initLoginPresenter(): LoginPresenter {

        val view = object : LoginView() {

            override fun success(t: UserDto, refreshData: Boolean) {
                toast(TAG, R.string.login_success)
                SpUtils.putString(context, BaseConfig.USER_NAME, t.username)
//                App.productUtils.startMainAty(context)
                finishAfterTransition()
            }

            override fun failed(string: String) {
                if (string.contains("账号")) {
                    tilUser.error = string
                } else if (string.contains("密码")) {
                    tilPassWord.error = string
                }
            }

            override fun complete(success: Boolean) {
                dismissLoadingDialog()
            }

        }

        return LoginPresenter(this, view)
    }

    private fun initLogoutPresenter(): LogoutPresenter {

        val view = object : LogoutView() {

            override fun success(refreshData: Boolean) {
                llLogout.visibility = View.GONE
                llLogin.visibility = View.VISIBLE
            }
        }

        return LogoutPresenter(this, view)
    }

    override fun dismiss(dialog: BasicDialog, clickCancel: Boolean) {
        //弹窗被手动关闭，取消登录
        loginPresenter.stop()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.imvBg -> hideSoftKeyboard(this)

            R.id.rlRegister -> showRegister(tilPassWord2.visibility != View.GONE)

            R.id.cardViewLogout -> logoutPresenter.logout()

            R.id.cardViewLogin -> {
                resetEditStatus()
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
