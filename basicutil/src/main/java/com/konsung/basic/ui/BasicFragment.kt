package com.konsung.basic.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.classic.common.MultipleStatusView
import com.konsung.basic.net.NetChangeReceiver
import com.konsung.basic.net.NetStateChangeObserver
import com.konsung.basic.net.NetworkType
import com.konsung.basic.util.Debug
import java.lang.ref.WeakReference

abstract class BasicFragment : Fragment(), NetStateChangeObserver {

    companion object {
        val TAG: String = BasicFragment::class.java.simpleName

        const val SHOW_NO_NETWORK = 0x001
        const val SHOW_LOADING = 0x002
        const val SHOW_ERROR = 0x003
        const val SHOW_CONTENT = 0x004
    }

    private var presenter: List<BasicPresenter>? = null
    private var loadHandler = false
    protected val myHandler: MyHandler by lazy { MyHandler(this) }
    protected var rootView: View? = null
    protected var resume = false
    private var firstShow = true
    protected val multiplyStatusView by lazy { rootView?.findViewById<MultipleStatusView>(getMultiplyId()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Debug.info(TAG, "BasicFragment onCreate $this")
        presenter = initPresenters()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Debug.info(TAG, "BasicFragment onCreateView $this")

        if (rootView == null) {
            rootView = layoutInflater.inflate(getLayoutId(), container, false)
            initView()
            multiplyStatusView?.apply {
                showLoading()
                setOnRetryClickListener {
                    myHandler.sendEmptyMessage(SHOW_LOADING)
                    refreshData()
                }
            }
        }

        return rootView
    }

    override fun onStart() {
        super.onStart()
        Debug.info(TAG, "BasicFragment onStart $this")
    }

    override fun onResume() {
        super.onResume()
        Debug.info(TAG, "BasicFragment onResume $this")
        NetChangeReceiver.registerObserver(this)

        resume = true
        resume()

        if (firstShow) {
            firstShow = false
            firstShow()
        } else {
            show()
        }
    }

    override fun onPause() {
        Debug.info(TAG, "BasicFragment onPause $this")
        super.onPause()
        NetChangeReceiver.unRegisterObserver(this)
        resume = false
        pause()
    }

    override fun onStop() {
        super.onStop()
        Debug.info(TAG, "BasicFragment onStop $this")
    }

    override fun onDestroy() {
        super.onDestroy()
        Debug.info(TAG, "BasicFragment onDestroy $this")

        if (loadHandler) {
            myHandler.removeCallbacksAndMessages(null)
        }

        presenter?.let {
            for (p in it) {
                p.destroy()
            }
        }
    }

    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)
    }

    /**
     * 处理handler消息
     */
    open fun handleMessage(msg: Message) {

    }

    /**
     * fragment第一次显示
     */
    open fun firstShow() {

    }

    /**
     * 除了第一次显示时，其他都会调用这个方法
     */
    open fun show() {

    }

    /**
     * fragment显示
     */
    open fun resume() {

    }

    /**
     * fragment隐藏
     */
    open fun pause() {

    }

    override fun onNetDisconnected() {

    }

    override fun onNetConnected(networkType: NetworkType) {

    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun initPresenters(): List<BasicPresenter>?

    /**
     * 初始化界面元素，整个生命周期中只会执行一次
     */
    abstract fun initView()

    abstract fun getMultiplyId(): Int

    /**
     * 数据获取失败之后刷新数据
     * @param
     */
    abstract fun refreshData()

    protected class MyHandler(fragment: BasicFragment) : Handler(Looper.getMainLooper()) {

        private var reference: WeakReference<BasicFragment> = WeakReference(fragment)

        init {
            val basicFragment = reference.get()
            basicFragment?.loadHandler = true
        }

        override fun handleMessage(msg: Message?) {

            if (msg == null) {
                return
            }

            val basicFragment = reference.get() ?: return

            when (msg.what) {

                SHOW_NO_NETWORK, SHOW_LOADING, SHOW_ERROR, SHOW_CONTENT -> {

                    removeMessages(SHOW_NO_NETWORK)
                    removeMessages(SHOW_LOADING)
                    removeMessages(SHOW_ERROR)
                    removeMessages(SHOW_CONTENT)

                    when (msg.what) {
                        SHOW_NO_NETWORK -> basicFragment.multiplyStatusView?.showNoNetwork()
                        SHOW_LOADING -> basicFragment.multiplyStatusView?.showLoading()
                        SHOW_ERROR -> basicFragment.multiplyStatusView?.showError()
                        SHOW_CONTENT -> basicFragment.multiplyStatusView?.showContent()
                    }
                    return
                }
            }

            basicFragment.handleMessage(msg)
        }
    }
}