package com.konsung.cla.demo2.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.konsung.basic.bean.HomeData
import com.konsung.basic.presenter.CollectLinkPresenterImpl
import com.konsung.basic.presenter.CollectLinkView
import com.konsung.basic.util.toast
import com.konsung.cla.demo2.R

/**
 * 添加站外文章收藏弹窗
 */
class LinkCollectDialog : DialogFragment(), CollectLinkView {

    companion object {
        val TAG: String = LinkCollectDialog::class.java.simpleName
    }

    private val collectPresenter by lazy { CollectLinkPresenterImpl(this) }

    private var llInput: LinearLayout? = null
    private var llLoading: LinearLayout? = null

    var collectLinkSuccess: CollectLinkSuccess? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        isCancelable = false

        val view = inflater.inflate(R.layout.dialog_link_collect, container, false)

        llInput = view.findViewById<LinearLayout>(R.id.llInput)
        llLoading = view.findViewById<LinearLayout>(R.id.llLoading)

        llInput?.visibility = View.VISIBLE
        llLoading?.visibility = View.GONE

        val etTitle = view.findViewById<TextInputEditText>(R.id.etTitle)
        val etAuthor = view.findViewById<TextInputEditText>(R.id.etAuthor)
        val etLink = view.findViewById<TextInputEditText>(R.id.etLink)
        val tvCancel = view.findViewById<TextView>(R.id.tvCancel)
        val tvSure = view.findViewById<TextView>(R.id.tvSure)

        tvCancel.setOnClickListener {
            //关闭弹窗
            dismissAllowingStateLoss()
        }

        tvSure.setOnClickListener {

            val title = etTitle.text.toString()
            if (title.isEmpty()) {
                etTitle.requestFocus()
                toast(TAG, R.string.title_is_null)
                return@setOnClickListener
            }

            val link = etLink.text.toString()
            if (link.isEmpty()) {
                etLink.requestFocus()
                toast(TAG, R.string.link_is_null)
                return@setOnClickListener
            }

            //向服务器发送收藏消息时，隐藏输入状态，显示正在加载的ui
            llInput?.visibility = View.INVISIBLE
            llLoading?.visibility = View.VISIBLE

            val author = etAuthor.text.toString()
            collectPresenter.collectLink(title, author, link)
        }

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        collectPresenter.destroy()
    }

    override fun collectLinkSuccess(t: HomeData.DatasBean) {
        collectLinkSuccess?.success(t)
        dismissAllowingStateLoss()
    }

    override fun collectLinkFailed(error: String) {
        //收藏失败的情况下恢复输入状态
        llInput?.visibility = View.VISIBLE
        llLoading?.visibility = View.GONE
    }

    override fun getUiContext(): Context? = context
}

/**
 * 通知收藏成功的接口
 */
interface CollectLinkSuccess {
    /**
     * 收藏成功
     * @param t 后台返回的这条收藏成功的数据
     */
    fun success(t: HomeData.DatasBean)
}