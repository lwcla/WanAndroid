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
import androidx.fragment.app.FragmentManager
import com.google.android.material.textfield.TextInputEditText
import com.konsung.basic.bean.site.SiteCollectBean
import com.konsung.basic.presenter.UiView
import com.konsung.basic.util.toast
import com.konsung.cla.demo2.R
import com.konsung.cla.demo2.presenter.AddSiteCollectPresenter
import com.konsung.cla.demo2.presenter.AddSiteCollectView

class SiteCollectDialog : DialogFragment(), UiView {

    companion object {
        val TAG: String = SiteCollectDialog::class.java.simpleName
    }

    private var llContent: LinearLayout? = null
    private var llLoading: LinearLayout? = null

    private val siteCollectPresenter2 by lazy { initSiteCollectPresenter2() }

    var siteCollectResult: SiteCollectResult? = null

    private var siteId = -1
    private var position = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        isCancelable = false

        val view = inflater.inflate(R.layout.dialog_site_collect, container, false)
        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        val tvSure = view.findViewById<TextView>(R.id.tvSure)
        val tvCancel = view.findViewById<TextView>(R.id.tvCancel)
        val etTitle = view.findViewById<TextInputEditText>(R.id.etTitle)
        val etLink = view.findViewById<TextInputEditText>(R.id.etLink)
        llContent = view.findViewById<LinearLayout>(R.id.llContent)
        llLoading = view.findViewById<LinearLayout>(R.id.llLoading)

        if (isAddNew()) {
            tvTitle.text = getString(R.string.add_new)
        } else {
            tvTitle.text = getString(R.string.edit_site)
        }

        tvSure.setOnClickListener {

            val title = etTitle.text.toString()
            if (title.isEmpty()) {
                toast(TAG, R.string.title_is_null)
                etTitle.requestFocus()
                return@setOnClickListener
            }

            val link = etLink.text.toString()
            if (link.isEmpty()) {
                toast(TAG, R.string.link_is_null)
                etLink.requestFocus()
                return@setOnClickListener
            }

            llContent?.visibility = View.INVISIBLE
            llLoading?.visibility = View.VISIBLE

            if (isAddNew()) {
                siteCollectPresenter2.addSite(title, link)
            } else {
                siteCollectPresenter2.editSite(id, title, link)
            }
        }

        tvCancel.setOnClickListener {
            dismissAllowingStateLoss()
        }

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        siteCollectPresenter2.destroy()
    }

    private fun isAddNew() = siteId == -1 || position == -1

    private fun initSiteCollectPresenter2(): AddSiteCollectPresenter {

        val view = object : AddSiteCollectView() {

            override fun success(t: SiteCollectBean, refreshData: Boolean) {
                if (isAddNew()) {
                    siteCollectResult?.addSuccess(t)
                } else {
                    siteCollectResult?.editSuccess(t, position)
                }

                dismissAllowingStateLoss()
            }

            override fun failed(string: String) {
                llContent?.visibility = View.VISIBLE
                llLoading?.visibility = View.GONE
            }
        }

        return AddSiteCollectPresenter(this, view)
    }

    fun show(manager: FragmentManager, tag: String?, siteId: Int = -1, position: Int = -1) {
        this.siteId = siteId
        this.position = position
        super.show(manager, tag)
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

    interface SiteCollectResult {
        fun addSuccess(t: SiteCollectBean)

        fun editSuccess(t: SiteCollectBean, position: Int)
    }
}