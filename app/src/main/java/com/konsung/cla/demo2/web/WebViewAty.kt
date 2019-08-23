package com.konsung.cla.demo2.web

import android.view.KeyEvent
import android.widget.RelativeLayout
import com.just.agentweb.AgentWeb
import com.konsung.basic.ui.BasicAty
import com.konsung.basic.ui.BasicPresenter
import com.konsung.basic.util.ToastUtils
import com.konsung.cla.demo2.R
import com.konsung.cla.demo2.config.Config.Companion.WEB_URL
import kotlinx.android.synthetic.main.activity_web.*


class WebViewAty : BasicAty() {

    companion object {
        val TAG: String = WebViewAty::class.java.simpleName
    }

    private lateinit var mAgentWeb: AgentWeb

    override fun getLayoutId(): Int = R.layout.activity_web

    override fun initView() {

        val url = intent.getStringExtra(WEB_URL)
        if (url.isNullOrEmpty()) {
            ToastUtils.instance.toast(context, TAG, R.string.data_error)
            finish()
            return
        }

//        val title = intent.getStringExtra(WEB_TITLE) ?: getString(R.string.unknown)
//        toolbar.title = title
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(rlWebView as RelativeLayout, RelativeLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go(url)
    }

    override fun initEvent() {

    }

    override fun initData() {

    }

    override fun onResume() {
        mAgentWeb.webLifeCycle.onResume()
        super.onResume()
    }

    override fun onPause() {
        mAgentWeb.webLifeCycle.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mAgentWeb.webLifeCycle.onDestroy()
        super.onDestroy()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {

        return if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            true
        } else super.onKeyDown(keyCode, event)
    }

    override fun initPresenter(): List<BasicPresenter>? = null

}