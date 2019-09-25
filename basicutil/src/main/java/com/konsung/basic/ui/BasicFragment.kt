package com.konsung.basic.ui

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.classic.common.MultipleStatusView
import com.konsung.basic.adapter.BaseAdapterHelper
import com.konsung.basic.net.NetChangeReceiver
import com.konsung.basic.net.NetStateChangeObserver
import com.konsung.basic.net.NetworkType
import com.konsung.basic.presenter.CollectPresenter
import com.konsung.basic.presenter.CollectView
import com.konsung.basic.receiver.CollectReceiver
import com.konsung.basic.receiver.CollectResult
import com.konsung.basic.util.Debug
import com.konsung.basic.util.R
import java.lang.ref.WeakReference

abstract class BasicFragment : Fragment(), UiView, NetStateChangeObserver, CollectResult {

    companion object {
        val TAG: String = BasicFragment::class.java.simpleName

        //这个时间不确定设置多少比较合适，设置为1秒以下起的作用不大，设置1秒以上会影响加载速度
        const val INIT_VIEW_DELAY_TIME = 0L

        const val SHOW_NO_NETWORK = 0x001
        const val SHOW_LOADING = 0x002
        const val SHOW_ERROR = 0x003
        const val SHOW_CONTENT = 0x004

        const val INIT_VIEW = 0X005
    }

    protected var multiplyStatusView: MultipleStatusView? = null
    protected var refreshRv: RefreshRecyclerView? = null

    private var presenter: List<BasicPresenter>? = null
    protected val collectPresenter by lazy { initCollectPresenter() }
    protected var dataListAdapterHelper: BaseAdapterHelper? = null

    private var loadHandler = false
    protected val myHandler: MyHandler by lazy { MyHandler(this) }
    private var rootView: View? = null
    protected var showView: View? = null
    protected var resume = false
    private var firstShow = true
    var fragmentRefresh: FragmentRefresh? = null
    var index: Int = 0
    //是否已经加载过contentView
    private var showedContent = false
    var needDelayInitView = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Debug.info(TAG, "BasicFragment onCreate $this")
        presenter = initPresenters()
        CollectReceiver.registerObserver(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Debug.info(TAG, "BasicFragment onCreateView $this")

        if (rootView == null) {
            rootView = layoutInflater.inflate(R.layout.view_multiplee_status_container, container, false)
            multiplyStatusView = rootView?.findViewById(R.id.multiplyStatusView)
            multiplyStatusView?.setOnRetryClickListener {
                showLoadView()
                resetData()
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
        Debug.info(TAG, "BasicFragment onResume $this firstShow?$firstShow ")
        NetChangeReceiver.registerObserver(this)

        resume = true

        if (firstShow) {
            multiplyStatusView?.showLoading()
//            Debug.info(TAG, "BasicFragment onResume showLoading needDelayInitView?$needDelayInitView")
            if (needDelayInitView) {
                myHandler.sendEmptyMessageDelayed(INIT_VIEW, INIT_VIEW_DELAY_TIME)
            } else {
                myHandler.sendEmptyMessage(INIT_VIEW)
            }
        } else {
            show()
        }
    }

    override fun onPause() {
        super.onPause()
        Debug.info(TAG, "BasicFragment onPause $this")
        myHandler.removeMessages(INIT_VIEW)
        NetChangeReceiver.unRegisterObserver(this)
        resume = false
        leave()
    }

    override fun onStop() {
        super.onStop()
        Debug.info(TAG, "BasicFragment onStop $this")
    }

    override fun onDestroy() {
        super.onDestroy()
        Debug.info(TAG, "BasicFragment onDestroy $this")
        CollectReceiver.unRegisterObserver(this)

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
    override fun collectResult(success: Boolean, collectId: Int, position: Int, toCollect: Boolean) {

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
     * 除了第一次显示时，其他都会调用这个方法
     */
    open fun show() {

    }

    /**
     * 刷新视图
     */
    open fun refreshView() {

    }

    /**
     * fragment隐藏
     */
    open fun leave() {

    }

    private fun initCollectPresenter(): CollectPresenter {

        val view = object : CollectView() {

            override fun failed(context: Context, string: String, position: Int, toCollect: Boolean) {
                dataListAdapterHelper?.refreshCollectStatus(position, !toCollect)
            }
        }

        return CollectPresenter(this, view)
    }

    override fun onNetDisconnected() {

    }

    override fun onNetConnected(networkType: NetworkType) {

    }

    override fun getUiContext(): Context? = context

    override fun showContentView() {
        myHandler.sendEmptyMessage(SHOW_CONTENT)
    }

    override fun showErrorView() {
        myHandler.sendEmptyMessage(SHOW_ERROR)
    }

    override fun showLoadView() {
        myHandler.sendEmptyMessage(SHOW_LOADING)
    }

    override fun showNoNetworkView() {
        myHandler.sendEmptyMessage(SHOW_NO_NETWORK)
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun initPresenters(): List<BasicPresenter>?

    /**
     * 初始化界面元素，整个生命周期中只会执行一次
     */
    abstract fun initView()

    abstract fun initEvent()

    abstract fun initData()

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

            val fragment = reference.get() ?: return

            when (msg.what) {

                SHOW_NO_NETWORK, SHOW_LOADING, SHOW_ERROR, SHOW_CONTENT -> {

                    if (msg.what != SHOW_CONTENT) {
                        //加载成功的话，由自己来处理
                        fragment.refreshRv?.finishRefresh()

                        if (msg.what == SHOW_LOADING) {
                            fragment.refreshRv?.finishLoadMore()
                        } else {
                            fragment.refreshRv?.finishLoadMore(0, false, false)
                        }
                    }

                    removeMessages(SHOW_NO_NETWORK)
                    removeMessages(SHOW_LOADING)
                    removeMessages(SHOW_ERROR)
                    removeMessages(SHOW_CONTENT)

                    when (msg.what) {
                        SHOW_NO_NETWORK -> fragment.multiplyStatusView?.showNoNetwork()
                        SHOW_LOADING -> fragment.multiplyStatusView?.showLoading()
                        SHOW_ERROR -> fragment.multiplyStatusView?.showError()

                        SHOW_CONTENT -> {

                            if (fragment.showView == null) {
                                sendEmptyMessage(SHOW_ERROR)
                                return
                            }

                            if (fragment.showedContent) {
                                fragment.multiplyStatusView?.showContent()
                            } else {
                                fragment.showedContent = true
                                val layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
                                fragment.multiplyStatusView?.showContent(fragment.showView, layoutParams)
                            }

                        }
                    }
                    return
                }

                INIT_VIEW -> {
                    Debug.info(TAG, "MyHandler handleMessage initView")
                    fragment.firstShow = false
                    fragment.showView = fragment.layoutInflater.inflate(fragment.getLayoutId(), null)

                    fragment.initView()
                    fragment.initEvent()
                    fragment.initData()
                }
            }

            fragment.handleMessage(msg)
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