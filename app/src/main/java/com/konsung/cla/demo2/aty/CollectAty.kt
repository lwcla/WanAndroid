package com.konsung.cla.demo2.aty

import android.view.View
import android.view.ViewStub
import android.widget.TextView
import com.chad.library.adapter.base.BaseViewHolder
import com.konsung.basic.adapter.BasicDataQuickAdapter
import com.konsung.basic.bean.HomeData
import com.konsung.basic.presenter.BasicPresenter
import com.konsung.basic.presenter.Presenter
import com.konsung.basic.ui.HomeDataAty
import com.konsung.cla.demo2.R
import com.konsung.cla.demo2.adapter.CollectListAdapter
import com.konsung.cla.demo2.dialog.CollectLinkSuccess
import com.konsung.cla.demo2.dialog.LinkCollectDialog
import com.konsung.cla.demo2.presenter.CollectListPresenter

/**
 * 我的收藏
 */
class CollectAty : HomeDataAty(), View.OnClickListener {
    companion object {

        val TAG: String = CollectAty::class.java.simpleName
    }

    init {
        refreshAfterScrollTop = false
        isCollectListPage = true
    }

    private val viewStub by lazy { findViewById<ViewStub>(R.id.viewStub) }

    private val collectListPresenter by lazy { CollectListPresenter(this, homeView) }

    private var linkCollectDialog: LinkCollectDialog? = null

    override fun initPresenter(): List<BasicPresenter>? = listOf(collectListPresenter, collectPresenter)

    override fun initPresenterList(): List<Presenter>? = null

    override fun initView() {
        viewStub.layoutResource = R.layout.view_collect_title
        val view = viewStub.inflate()
        val addCollect = view.findViewById<TextView>(R.id.addCollect)
        addCollect.setOnClickListener(this)
        super.initView()
    }

    private fun showLinkCollectDialog() {

        val tag = LinkCollectDialog.TAG

        linkCollectDialog = supportFragmentManager.findFragmentByTag(tag) as LinkCollectDialog?

        if (linkCollectDialog != null) {
            supportFragmentManager.beginTransaction().remove(linkCollectDialog!!).commitAllowingStateLoss()
        }

        linkCollectDialog = null
        linkCollectDialog = LinkCollectDialog()

        linkCollectDialog?.collectLinkSuccess = object : CollectLinkSuccess {

            override fun success(t: HomeData.DatasBean) {
                dataAdapter?.addData(0, t)
                refreshRv.refreshDataAfterScrollTop()
            }
        }

        if (!linkCollectDialog!!.isAdded) {
            linkCollectDialog!!.show(supportFragmentManager, tag)
        }
    }

    override fun getAtyTitle(): String = ""

    override fun getImvStartId(): Int = R.id.imvStart

    override fun initAdapter(t: List<HomeData.DatasBean>): BasicDataQuickAdapter<BaseViewHolder> = CollectListAdapter(context, t)

    override fun loadMoreData() {
        collectListPresenter.loadMore()
    }

    override fun resetData() {
        collectListPresenter.refresh()
    }

    override fun onClick(v: View) {

        when (v.id) {
            //添加收藏
            R.id.addCollect -> showLinkCollectDialog()
        }
    }
}