package com.konsung.basic.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

abstract class BasicDialog : DialogFragment() {

    companion object {
        val TAG = BasicDialog::class.java.simpleName
    }

    var rootView: View? = null
    public var dismissListener: DismissListener? = null
    private var cancel: Boolean = true
    //点击取消按钮
    protected var clickCancel = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(getLayoutId(), container, false)
        isCancelable = cancel
        initView(rootView!!)
        return rootView
    }

    override fun onStart() {
        super.onStart()

        if (context == null) {
            dismissAllowingStateLoss()
            return
        }

        rootView?.let {
            initData(it)
        }
    }

    override fun onStop() {
        super.onStop()
        dismissListener?.dismiss(this, clickCancel)
    }

    fun show(manager: FragmentManager, tag: String?, cancel: Boolean) {
        this.cancel = cancel
        super.show(manager, tag)
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun initView(view: View)

    abstract fun initData(view: View)
}

interface DismissListener {

    fun dismiss(dialog: BasicDialog, artificial: Boolean)

}