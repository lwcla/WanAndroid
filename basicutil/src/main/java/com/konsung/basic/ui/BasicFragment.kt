package com.konsung.basic.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.classic.common.MultipleStatusView
import com.konsung.basic.config.BaseConfig
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

    protected val multiplyStatusView by lazy { rootView?.findViewById<MultipleStatusView>(getMultiplyId()) }
    private var presenter: List<BasicPresenter>? = null
    private var loadHandler = false
    protected val myHandler: MyHandler by lazy { MyHandler(this) }
    protected var rootView: View? = null
    protected var resume = false
    private var firstShow = true
    var fragmentRefresh: FragmentRefresh? = null
    var index: Int = 0

    private val localReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            if (context == null || intent == null) {
                return
            }

            if (intent.action == BaseConfig.COLLECT_RESULT_ACTION) {
                //收藏结果

                val collectResult = intent.getBooleanExtra(BaseConfig.COLLECT_RESULT, false)
                val collectDataPosition = intent.getIntExtra(BaseConfig.COLLECT_DATA_POSITION, -1)
                val toCollect = intent.getBooleanExtra(BaseConfig.TO_COLLECT, false)
                val collectId = intent.getIntExtra(BaseConfig.COLLECT_ID, -1)

                if (collectDataPosition < 0 || collectId < 0) {
                    return
                }

                collectResult(collectResult, collectId, collectDataPosition, toCollect)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Debug.info(TAG, "BasicFragment onCreate $this")
        presenter = initPresenters()

        val intentFilter = IntentFilter(BaseConfig.COLLECT_RESULT_ACTION)
        context?.let {
            LocalBroadcastManager.getInstance(it).registerReceiver(localReceiver, intentFilter)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Debug.info(TAG, "BasicFragment onCreateView $this")

        if (rootView == null) {
            rootView = layoutInflater.inflate(getLayoutId(), container, false)
            initView()
            multiplyStatusView?.apply {
                showLoading()
                setOnRetryClickListener {
                    showLoadView()
                    resetData()
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
        super.onPause()
        Debug.info(TAG, "BasicFragment onPause $this")
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
        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(localReceiver)
        }

        if (loadHandler) {
            myHandler.removeCallbacksAndMessages(null)
        }

        presenter?.let {
            for (p in it) {
                p.destroy()
            }
        }
    }

    /**
     * 收藏结果
     */
    open fun collectResult(success: Boolean, collectId: Int, position: Int, toCollect: Boolean) {

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
     * 刷新视图
     */
    open fun refreshView() {

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

    protected fun showContentView() {
        myHandler.sendEmptyMessage(SHOW_CONTENT)
    }

    protected fun showErrorView() {
        myHandler.sendEmptyMessage(SHOW_ERROR)
    }

    protected fun showLoadView() {
        myHandler.sendEmptyMessage(SHOW_LOADING)
    }

    protected fun showNoNetworkView() {
        myHandler.sendEmptyMessageDelayed(SHOW_NO_NETWORK, 1000)
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun initPresenters(): List<BasicPresenter>?

    /**
     * 初始化界面元素，整个生命周期中只会执行一次
     */
    abstract fun initView()

    /**
     * 切换当前ui状态的布局id,不过可以强制设置为同一个
     */
    abstract fun getMultiplyId(): Int

    /**
     * 数据获取失败之后刷新数据
     * @param
     */
    abstract fun resetData()

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


/**
 * fragment刷新接口
 */
interface FragmentRefresh {
    /**
     * 现在是否为可以刷新的状态
     * @param isRefresh true表示现在recyclerView已经滚动了一段距离了，可以刷新
     * @param index 表示当前fragment的位置
     */
    fun refresh(isRefresh: Boolean, index: Int)
}