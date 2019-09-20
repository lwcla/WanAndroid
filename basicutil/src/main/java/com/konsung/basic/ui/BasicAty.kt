package com.konsung.basic.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.konsung.basic.dialog.BasicDialog
import com.konsung.basic.dialog.DismissListener
import com.konsung.basic.dialog.LoadingDialog
import com.konsung.basic.util.Debug
import com.konsung.basic.util.R
import com.konsung.basic.util.StatusBarUtil
import java.lang.ref.WeakReference


abstract class BasicAty : AppCompatActivity(), UiView, DismissListener {

    companion object {
        val TAG: String = BasicAty::class.java.simpleName
    }

    protected var initDelay = 0L //用来延迟初始化界面，如果延迟了初始化，那么在onStart,onResume中处理界面相关的数据时，就需要特殊处理
    private var presenters: List<BasicPresenter>? = null
    protected lateinit var context: Context
    private var loadingDialog: LoadingDialog? = null
    protected val myHandler by lazy{ MyHandler(this)}
    private val initRunnable = Runnable {
        initView()
        initEvent()
        initData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        Debug.info(TAG, "BasicAty onCreate $this")
        initStatusBar()
        context = this

        Debug.info(TAG, "BasicAty onCreate initDelay=$initDelay")
        presenters = initPresenter()
        if (initDelay > 0) {
            myHandler.postDelayed(initRunnable, initDelay)
        } else {
            runOnUiThread(initRunnable)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Debug.info(TAG, "BasicAty onNewIntent $this")
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

    fun dismissLoadingDialog() {
        if (loadingDialog != null && loadingDialog?.isVisible == true) {
            loadingDialog?.dismissAllowingStateLoss()
        }

        loadingDialog = null
    }

    /**
     * 设置状态栏颜色
     */
    protected fun settStatusBarColor(@ColorRes colorRes: Int) {
        StatusBarUtil.setStatusBarColor(this, ContextCompat.getColor(this, colorRes))
    }

    fun handleMessage(msg: Message) {

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

    fun showLoadingDialog(@StringRes textRes: Int = R.string.loading_please_wait, cancel: Boolean = true) {

        val tag = LoadingDialog::class.java.simpleName

        try {
            loadingDialog = supportFragmentManager.findFragmentByTag(tag) as LoadingDialog?
        } catch (e: Exception) {
            e.printStackTrace()
        }

        loadingDialog?.let {
            supportFragmentManager.beginTransaction().remove(it).commitAllowingStateLoss()
        }

        if (loadingDialog == null) {
            loadingDialog = LoadingDialog()
            loadingDialog?.dismissListener = this
        }

        if (loadingDialog?.isAdded != true) {
            loadingDialog?.show(supportFragmentManager, tag, textRes, cancel)
        }
    }

    override fun dismiss(dialog: BasicDialog, clickCancel: Boolean) {

    }

    override fun getUiContext(): Context? = context

    override fun showContentView() {

    }

    override fun showErrorView() {

    }

    override fun showLoadView() {

    }

    override fun showNoNetworkView() {

    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun initPresenter(): List<BasicPresenter>?

    abstract fun initView()

    abstract fun initEvent()

    abstract fun initData()

    class MyHandler(activity: BasicAty) : Handler() {

        private val reference: WeakReference<BasicAty> = WeakReference(activity)

        override fun handleMessage(msg: Message?) {

            if (msg == null) {
                return
            }

            val aty = reference.get() ?: return

            aty.handleMessage(msg)
        }
    }
}