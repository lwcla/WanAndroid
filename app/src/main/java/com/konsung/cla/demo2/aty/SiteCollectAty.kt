package com.konsung.cla.demo2.aty

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.konsung.basic.bean.site.SiteCollectBean
import com.konsung.basic.ui.BasicAty
import com.konsung.basic.ui.BasicPresenter
import com.konsung.basic.ui.RefreshRecyclerView
import com.konsung.basic.ui.SpaceDecoration
import com.konsung.basic.view.MultipleStatusView
import com.konsung.cla.demo2.R
import com.konsung.cla.demo2.adapter.EditListener
import com.konsung.cla.demo2.adapter.SiteCollectAdapter
import com.konsung.cla.demo2.dialog.SiteCollectDialog
import com.konsung.cla.demo2.presenter.SiteCollectPresenter
import com.konsung.cla.demo2.presenter.SiteCollectView
import kotlinx.android.synthetic.main.activity_site_collect.*

/**
 * 收藏的网站
 */
class SiteCollectAty : BasicAty(), View.OnClickListener {

    private val multiplyStatusView by lazy { findViewById<MultipleStatusView>(R.id.multiplyStatusView) }
    private val refreshRv by lazy { multiplyStatusView.findViewById<RefreshRecyclerView>(R.id.refreshRv) }

    private val siteCollectPresenter by lazy { initSiteCollectPresenter() }

    private var siteCollectAdapter: SiteCollectAdapter? = null
    private var siteCollectDialog: SiteCollectDialog? = null


    override fun getLayoutId(): Int = R.layout.activity_site_collect

    override fun initPresenter(): List<BasicPresenter>? = listOf(siteCollectPresenter)

    override fun initView() {
        showLoadView()
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun initEvent() {
        tvAdd.setOnClickListener(this)
        toolbar.setNavigationOnClickListener { finish() }
        multiplyStatusView.setOnRetryClickListener {
            showLoadView()
            initData()
        }

        fab.setOnClickListener {
            refreshRv.refreshDataAfterScrollTop()
        }
    }

    override fun initData() {
        siteCollectPresenter.refresh()
    }

    private fun setAdapter(list: List<SiteCollectBean>) {

        if (siteCollectAdapter == null) {

            siteCollectAdapter = SiteCollectAdapter(list)
            siteCollectAdapter?.editListener = object : EditListener {

                override fun edit(id: Int, position: Int) {
                    showSiteCollectDialog(id, position)
                }
            }

            refreshRv.apply {
                setEnableLoadMore(false)
                refreshAfterScrollTop = false
                setOnRefreshListener {
                    initData()
                }

                rv.let {
                    fab.attachToRecyclerView(it)
                    it.itemAnimator?.changeDuration = 0
                    val space = context.resources.getDimension(R.dimen.dp_10)
                    rv.addItemDecoration(SpaceDecoration(space.toInt(), false))
                    val manager = LinearLayoutManager(context)
                    rv.layoutManager = manager

                    it.adapter = siteCollectAdapter
                }
            }
        } else {
            siteCollectAdapter?.update(list)
        }
    }

    private fun showSiteCollectDialog(id: Int = -1, position: Int = -1) {

        val tag = SiteCollectDialog.TAG

        siteCollectDialog = supportFragmentManager.findFragmentByTag(tag) as SiteCollectDialog?

        siteCollectDialog?.let {
            supportFragmentManager.beginTransaction().remove(it).commitAllowingStateLoss()
        }

        siteCollectDialog = null
        siteCollectDialog = SiteCollectDialog()

        siteCollectDialog?.siteCollectResult = object : SiteCollectDialog.SiteCollectResult {

            override fun addSuccess(t: SiteCollectBean) {
                siteCollectAdapter?.addData(t)
                refreshRv?.refreshDataAfterScrollTop()
            }

            override fun editSuccess(t: SiteCollectBean, position: Int) {

            }
        }

        if (!siteCollectDialog!!.isAdded) {
            siteCollectDialog!!.show(supportFragmentManager, tag, id, position)
        }

    }

    private fun initSiteCollectPresenter(): SiteCollectPresenter {

        val view = object : SiteCollectView() {

            override fun success(t: List<SiteCollectBean>, refreshData: Boolean) {
                setAdapter(t)
            }
        }

        return SiteCollectPresenter(this, view)
    }

    override fun showContentView() {
        multiplyStatusView.showContent()
    }

    override fun showEmptyView() {
        multiplyStatusView.showEmpty()
    }

    override fun showErrorView() {
        multiplyStatusView.showError()
    }

    override fun showLoadView() {
        multiplyStatusView.showLoading()
    }

    override fun showNoNetworkView() {
        multiplyStatusView.showNoNetwork()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tvAdd -> showSiteCollectDialog()
        }
    }
}