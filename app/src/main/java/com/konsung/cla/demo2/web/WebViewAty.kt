package com.konsung.cla.demo2.web

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

    override fun getLayoutId(): Int = R.layout.activity_web

    override fun initPresenter(): List<BasicPresenter>? = null

    override fun initView() {

    }

    override fun initEvent() {

    }

    override fun initData() {
        val url = intent.getStringExtra(WEB_URL)
        if (url.isNullOrEmpty()) {
            ToastUtils.instance.toast(context, TAG, R.string.data_error)
            finish()
            return
        }
        webView.loadUrl(url)
    }

}