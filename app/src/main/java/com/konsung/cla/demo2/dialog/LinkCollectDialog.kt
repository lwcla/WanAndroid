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
import com.konsung.basic.presenter.CollectLinkPresenter
import com.konsung.basic.presenter.CollectLinkView
import com.konsung.basic.ui.UiView
import com.konsung.basic.util.toast
import com.konsung.cla.demo2.R

class LinkCollectDialog : DialogFragment(), UiView {

    companion object {
        val TAG: String = LinkCollectDialog::class.java.simpleName
    }

    private val collectPresenter by lazy { initCollectPresenter() }

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


            llInput?.visibility = View.INVISIBLE
            llLoading?.visibility = View.VISIBLE

            val author = etAuthor.text.toString()
            collectPresenter.collectLink(title, author, link)
        }

        return view
    }

    private fun initCollectPresenter(): CollectLinkPresenter {

        val view = object : CollectLinkView() {

            override fun success(t: HomeData.DatasBean, refreshData: Boolean) {
                collectLinkSuccess?.success(t)
                dismissAllowingStateLoss()
            }

            override fun failed(string: String) {
                llInput?.visibility = View.VISIBLE
                llLoading?.visibility = View.GONE
            }
        }

        return CollectLinkPresenter(this, view)
    }

    override fun getUiContext(): Context? = context

    override fun loadComplete(success: Boolean) {

    }

    override fun showContentView() {

    }

    override fun showErrorView() {

    }

    override fun showNoNetworkView() {

    }

    override fun showLoadView() {

    }

    override fun showEmptyView() {

    }

    interface CollectLinkSuccess {
        fun success(t: HomeData.DatasBean)
    }
}