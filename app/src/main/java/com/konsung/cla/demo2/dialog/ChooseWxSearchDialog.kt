package com.konsung.cla.demo2.dialog

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.konsung.basic.bean.project.ProjectTitleBean
import com.konsung.basic.presenter.LoadWxArticleTitle
import com.konsung.basic.presenter.LoadWxArticleTitleView
import com.konsung.basic.ui.UiView
import com.konsung.basic.util.ConvertUtils
import com.konsung.basic.util.toast
import com.konsung.cla.demo2.R
import com.konsung.cla.demo2.adapter.WxArticleTitleAdapter
import kotlinx.android.synthetic.main.dialog_switch_wx_search.*

/**
 * 搜索公众号时，切换公众号的弹窗
 */
class ChooseWxSearchDialog : BottomSheetDialogFragment(), UiView {


    companion object {
        val TAG: String = ChooseWxSearchDialog::class.java.simpleName
    }

    private var rootView: View? = null

    private val loadWxArticleTitle by lazy { initLoadWxArticleTitle() }

    private var wxArticleTitleAdapter: WxArticleTitleAdapter? = null
    var chooseWxArticleNameListener: ChooseWxArticleNameListener? = null

    private var chooseWx: ProjectTitleBean? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        context?.let {
            (rootView?.parent as? View)?.setBackgroundColor(ContextCompat.getColor(it, android.R.color.transparent))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.dialog_switch_wx_search, container, false)

        val tvCancel = rootView!!.findViewById<TextView>(R.id.tvCancel)
        tvCancel.setOnClickListener {
            dismissAllowingStateLoss()
        }

        return rootView
    }

    override fun onStart() {
        super.onStart()
        loadWxArticleTitle.load()
    }

    override fun onStop() {
        super.onStop()
        chooseWxArticleNameListener?.choose(chooseWx?.name, chooseWx?.id)
    }

    override fun onDestroy() {
        super.onDestroy()
        loadWxArticleTitle.destroy()
    }

    private fun setWxTitle(t: List<ProjectTitleBean>) {
        val chipsLayoutManager = ChipsLayoutManager.newBuilder(context)
                //set vertical gravity for all items in a row. Default = Gravity.CENTER_VERTICAL
                .setChildGravity(Gravity.LEFT)
                //whether RecyclerView can scroll. TRUE by default
                .setScrollingEnabled(true)
                //a layoutOrientation of layout manager, could be VERTICAL OR HORIZONTAL.
                // HORIZONTAL by default
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                // row strategy for views in completed row, could be STRATEGY_DEFAULT,
                // STRATEGY_FILL_VIEW,
                //STRATEGY_FILL_SPACE or STRATEGY_CENTER
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                // whether strategy is applied to last row. FALSE by default
                .withLastRow(true)
                .build()

        rvWxArticleTitle.layoutManager = chipsLayoutManager

        val horizontalSpacing = ConvertUtils.dp2px(context!!, 25.toFloat())
        val verticalSpacing = ConvertUtils.dp2px(context!!, 15.toFloat())
        rvWxArticleTitle.addItemDecoration(SpacingItemDecoration(horizontalSpacing, verticalSpacing))

        wxArticleTitleAdapter = WxArticleTitleAdapter(context!!, t)
        wxArticleTitleAdapter?.setOnItemClickListener { adapter, view, position ->

            if (position < 0 || position >= t.size) {
                toast(TAG, R.string.data_error)
                dismissAllowingStateLoss()
                return@setOnItemClickListener
            }

            val bean = t[position]
            chooseWx = bean
            dismissAllowingStateLoss()
        }

        rvWxArticleTitle.adapter = wxArticleTitleAdapter

        rvWxArticleTitle.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    private fun initLoadWxArticleTitle(): LoadWxArticleTitle {

        val view = object : LoadWxArticleTitleView() {

            override fun success(t: List<ProjectTitleBean>, refreshData: Boolean) {

                if (t.isEmpty()) {
                    toast(TAG, R.string.data_error)
                    dismissAllowingStateLoss()
                    return
                }

                setWxTitle(t)
            }
        }

        return LoadWxArticleTitle(this, view)
    }

    override fun getUiContext(): Context? = context

    override fun showContentView() {

    }

    override fun showErrorView() {
        dismissAllowingStateLoss()
    }

    override fun showNoNetworkView() {
        dismissAllowingStateLoss()
    }

    override fun showLoadView() {

    }
}

interface ChooseWxArticleNameListener {
    fun choose(name: String?, id: Int?)
}