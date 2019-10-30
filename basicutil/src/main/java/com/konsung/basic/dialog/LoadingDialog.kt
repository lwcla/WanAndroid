package com.konsung.basic.dialog

import android.view.View
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.konsung.basic.util.R

class LoadingDialog : BasicDialog() {

    private var tvTitle: TextView? = null
    private var tvCancel: TextView? = null
    private var textRes: Int = R.string.loading_please_wait

    override fun getLayoutId(): Int = R.layout.dialog_loading

    override fun initView(view: View) {
        tvTitle = view.findViewById(R.id.tvTitle)
        tvCancel = view.findViewById(R.id.tvCancel)
        tvCancel?.setOnClickListener {
            clickCancel = true
            dismissAllowingStateLoss()
        }
    }

    override fun initData(view: View) {
        try {
            tvTitle?.setText(textRes)
        } catch (e: Exception) {
            tvTitle?.setText(R.string.loading_please_wait)
        }
    }

    fun show(manager: FragmentManager, tag: String?, textRes: Int = R.string.loading_please_wait, cancel: Boolean = true) {
        this.textRes = textRes
        super.show(manager, tag, cancel)
    }
}