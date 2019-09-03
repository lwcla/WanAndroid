package com.konsung.basic.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.konsung.basic.util.Debug
import com.konsung.basic.util.R
import com.konsung.basic.util.StatusBarUtil


abstract class BasicAty : AppCompatActivity() {

    companion object {
        val TAG: String = BasicAty::class.java.simpleName
    }

    private var presenters: List<BasicPresenter>? = null
    protected lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        Debug.info(TAG, "BasicAty onCreate $this")
        initStatusBar()
        context = this

        presenters = initPresenter()
        initView()
        initEvent()
        initData()
    }

    override fun onStart() {
        super.onStart()
        Debug.info(TAG, "BasicAty onStart $this")
    }

    override fun onResume() {
        super.onResume()
        Debug.info(TAG, "BasicAty onResume $this")
    }

    override fun onPause() {
        super.onPause()
        Debug.info(TAG, "BasicAty onPause $this")
    }

    override fun onStop() {
        super.onStop()
        Debug.info(TAG, "BasicAty onStop $this")
    }

    override fun onDestroy() {
        super.onDestroy()
        Debug.info(TAG, "BasicAty onDestroy $this")
        presenters?.let {
            for (p in it) {
                p.destroy()
            }
        }
    }

    private fun initStatusBar() {
        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this, true)
        //设置状态栏透明
        StatusBarUtil.setStatusBarColor(this, ContextCompat.getColor(this, R.color.red))
        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
        //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
//        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
//            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
//            //这样半透明+白=灰, 状态栏的文字能看得清
//            StatusBarUtil.setStatusBarColor(this,0x55000000);
//        }
    }

    /**
     * 设置状态栏颜色
     */
    protected fun settStatusBarColor(@ColorRes colorRes: Int) {
        StatusBarUtil.setStatusBarColor(this, ContextCompat.getColor(this, colorRes))
    }


    /**
     * 隐藏软键盘(只适用于Activity，不适用于Fragment)
     */
    fun hideSoftKeyboard(activity: Activity) {
        val view = activity.currentFocus
        view?.let {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    fun showKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        if (imm != null) {
            view.requestFocus()
            imm.showSoftInput(view, 0)
        }
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun initPresenter(): List<BasicPresenter>?

    abstract fun initView()

    abstract fun initEvent()

    abstract fun initData()
}