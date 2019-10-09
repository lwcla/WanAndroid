package com.konsung.cla.demo2.dialog

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
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

        //选择用作者昵称筛选
        const val AUTHOR_NAME = 0x555
    }

    private var rootView: View? = null

    private val loadWxArticleTitle by lazy { initLoadWxArticleTitle() }

    var chooseWxArticleNameListener: ChooseWxArticleNameListener? = null

    private var chooseWx: ProjectTitleBean? = null

    private var selectId = -1
    private var click = false

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

        val bean = ProjectTitleBean(null, -1, AUTHOR_NAME, getString(R.string.author_name), -1, -1, false, -1)
        val list = listOf(bean)
        setAdapter(list, rvOther)
    }

    override fun onStop() {
        super.onStop()
        if (click) {
            val title = when (chooseWx?.id ?: -1) {
                //什么都没选
                -1 -> chooseWx?.name
                //选择用作者昵称筛选
                AUTHOR_NAME -> getString(R.string.author_name)
                //公众号筛选
                else -> getString(R.string.official_accounts) + " - " + chooseWx?.name
            }
            chooseWxArticleNameListener?.choose(title, chooseWx?.id)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        loadWxArticleTitle.destroy()
    }

    private fun setAdapter(t: List<ProjectTitleBean>, rv: RecyclerView) {
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

        rv.layoutManager = chipsLayoutManager

        val horizontalSpacing = ConvertUtils.dp2px(context!!, 25.toFloat())
        val verticalSpacing = ConvertUtils.dp2px(context!!, 15.toFloat())
        rv.addItemDecoration(SpacingItemDecoration(horizontalSpacing, verticalSpacing))

        val titleAdapter = WxArticleTitleAdapter(context!!, t, selectId)
        titleAdapter.setOnItemClickListener { _, _, position ->

            if (position < 0 || position >= t.size) {
                toast(TAG, R.string.data_error)
                dismissAllowingStateLoss()
                return@setOnItemClickListener
            }

            val bean = t[position]

            //这是取消之前的选择
            if (bean.id == selectId) {
                chooseWx = bean
                chooseWx?.id = -1
            } else {
                chooseWx = bean
            }

            click = true
            dismissAllowingStateLoss()
        }

        rv.adapter = titleAdapter
        rv.visibility = View.VISIBLE
    }

    private fun setWxTitle(t: List<ProjectTitleBean>) {
        setAdapter(t, rvWxArticleTitle)
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

    override fun loadComplete(success: Boolean) {
        if (!success) {
            dismissAllowingStateLoss()
        }
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

    fun show(manager: FragmentManager, tag: String?, selectId: Int) {
        this.selectId = selectId
        super.show(manager, tag)
    }
}

interface ChooseWxArticleNameListener {
    fun choose(name: String?, id: Int?)
}