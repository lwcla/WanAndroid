package com.konsung.cla.demo2.web

import android.content.Context
import android.view.KeyEvent
import android.view.View
import android.webkit.WebView
import android.widget.RelativeLayout
import com.just.agentweb.AgentWeb
import com.just.agentweb.DefaultWebClient
import com.just.agentweb.WebChromeClient
import com.konsung.basic.config.BaseConfig.Companion.IS_COLLECT
import com.konsung.basic.config.BaseConfig.Companion.NEED_COLLECT
import com.konsung.basic.config.BaseConfig.Companion.WEB_ARTICLE_ID
import com.konsung.basic.config.BaseConfig.Companion.WEB_DATA_POSITION
import com.konsung.basic.config.BaseConfig.Companion.WEB_TITLE
import com.konsung.basic.config.BaseConfig.Companion.WEB_URL
import com.konsung.basic.ui.BasicAty
import com.konsung.basic.ui.BasicPresenter
import com.konsung.basic.util.StringUtils
import com.konsung.basic.util.ToastUtils
import com.konsung.basic.util.toast
import com.konsung.cla.demo2.R
import com.konsung.cla.demo2.view.ShareDialog
import com.konsung.cla.demo2.view.ShareDialogListener
import kotlinx.android.synthetic.main.activity_web.*


class WebViewAty : BasicAty(), View.OnClickListener, ShareDialogListener {

    companion object {
        val TAG: String = WebViewAty::class.java.simpleName
    }

    private var mAgentWeb: AgentWeb? = null
    private val collectPresenter by lazy { initCollectPresenter() }
    private lateinit var link: String
    private var artId: Int = -1
    private var dataPosition: Int = -1
    private var collect = false
    private var needCollect = false
    private var shareDialog: ShareDialog? = null

    override fun getLayoutId(): Int = R.layout.activity_web

    override fun initView() {

        val url = intent.getStringExtra(WEB_URL)
        val articleId = intent.getIntExtra(WEB_ARTICLE_ID, -1)
        dataPosition = intent.getIntExtra(WEB_DATA_POSITION, -1)
        collect = intent.getBooleanExtra(IS_COLLECT, false)
        needCollect = intent.getBooleanExtra(NEED_COLLECT, false)

        if (url.isNullOrEmpty() || articleId == -1) {
            ToastUtils.instance.toast(context, TAG, R.string.data_error)
            finish()
            return
        }

        link = url
        artId = articleId

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        tvTitle.isSelected = true
        StringUtils.instance.loadTextIcon(context, tvMore)

        val webChromeClient: WebChromeClient? = object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView, title: String) {
                super.onReceivedTitle(view, title)
                tvTitle.text = StringUtils.instance.formHtml(title)
            }
        }

        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(rlWebView as RelativeLayout, RelativeLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .setWebChromeClient(webChromeClient)
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)
                .createAgentWeb()
                .ready()
                .go(url)
    }

    override fun initData() {
        val title = intent.getStringExtra(WEB_TITLE) ?: getString(R.string.unknown)
        tvTitle.text = StringUtils.instance.formHtml(title)
    }

    override fun initEvent() {
        tvMore.setOnClickListener(this)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        mAgentWeb?.webLifeCycle?.onResume()
        super.onResume()
    }

    override fun onPause() {
        mAgentWeb?.webLifeCycle?.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mAgentWeb?.webLifeCycle?.onDestroy()
        super.onDestroy()
    }

    private fun initCollectPresenter(): CollectPresenter {

        val view = object : CollectView() {

            override fun success(context: Context, position: Int, toCollect: Boolean) {
                this@WebViewAty.collect = toCollect
            }

            override fun failed(context: Context, string: String, position: Int, toCollect: Boolean) {
                this@WebViewAty.collect = toCollect
            }
        }

        val presenter = CollectPresenter(context, view)
        presenter.sendMessage = true

        return presenter
    }

    /**
     * 收藏
     */
    override fun collect() {

        if (artId < 0) {
            toast(TAG, R.string.data_error)
            return
        }

        val b = collectPresenter.collect(dataPosition, artId, collect)
        if (b) {
            collect = !collect
        }
    }

    override fun initPresenter(): List<BasicPresenter>? = listOf(collectPresenter)

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {

        if (mAgentWeb != null && mAgentWeb!!.handleKeyEvent(keyCode, event)) {
            return true
        }

        return super.onKeyDown(keyCode, event)
    }

    override fun onClick(v: View) {
        when (v.id) {

            R.id.tvMore -> {
                shareDialog = ShareDialog()
                shareDialog?.let {
                    it.shareDialogListener = this
                    it.collectFlag = collect
                    it.needCollect = needCollect
                    if (!it.isAdded) {
                        it.show(supportFragmentManager, ShareDialog::class.java.simpleName, link)
                    }
                }
            }
        }
    }
}