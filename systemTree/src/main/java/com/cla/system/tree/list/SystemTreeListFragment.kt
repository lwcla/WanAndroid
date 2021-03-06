package com.cla.system.tree.list

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.cla.system.tree.R
import com.cla.system.tree.list.adapter.SystemTreeListAdapter
import com.konsung.basic.bean.tree.SystemTreeListBean
import com.konsung.basic.bean.tree.SystemTreeTitle
import com.konsung.basic.presenter.Presenter
import com.konsung.basic.ui.fragment.BasicFragment
import com.konsung.basic.util.AppUtils
import com.konsung.basic.view.SpaceDecoration

/**
 * 体系分类列表
 */
class SystemTreeListFragment : BasicFragment(), LoadSystemTreeListView {

    private val loadTreeList: LoadSystemTreeListPresenter by lazy { LoadSystemTreeListPresenterImpl(this) }
    private val systemAdapter = SystemTreeListAdapter(mutableListOf())

    override fun getLayoutId(): Int = R.layout.view_fresh_rv


    override fun initPresenterList(): List<Presenter>? = listOf(loadTreeList)

    override fun initView() {
        refreshRv = showView?.findViewById(R.id.refreshRv)
        refreshRv?.setEnableLoadMore(false)

        refreshRv?.rv?.apply {
            val manager = LinearLayoutManager(context)
            val space = context.resources.getDimension(com.konsung.basic.util.R.dimen.dp_8)
            addItemDecoration(SpaceDecoration(space.toInt(), false))
            itemAnimator?.changeDuration = 0

//            systemAdapter.setHasStableIds(true)
            adapter = systemAdapter

            layoutManager = manager
        }

        refreshRv?.setRefreshListener(fragmentRefresh, index) {
            refreshRv?.autoRefresh()
            resetData()
        }
    }

    override fun initEvent() {
        refreshRv?.setOnRefreshListener {
            resetData()
        }

        systemAdapter.clickListener = View.OnClickListener {

            val tvTitle = it.findViewById<TextView>(R.id.tvName)

            val bean = it.getTag(R.id.recycler_view_adapter_item_click) as? SystemTreeListBean
                    ?: return@OnClickListener

            val map = mutableMapOf<String, Int>()
            for (b in bean.children) {
                map[b.name] = b.id
            }

            val position = it.getTag(R.id.recycler_view_adapter_item_click_position) as? Int ?: -1

            val systemTreeTitle = SystemTreeTitle(bean.name, map, position)
            AppUtils.startSystemTreeDetailAty(activity, tvTitle, systemTreeTitle)
        }
    }

    override fun initData() {
        resetData()
    }

    override fun resetData() {
        loadTreeList.loadSystemTreeList()
    }

    override fun refreshView() {
        refreshRv?.refreshDataAfterScrollTop()
    }

    override fun loadSystemTreeListSuccess(list: List<SystemTreeListBean>) {
        systemAdapter.update(list)

        refreshRv?.finishRefresh()
    }

    override fun loadSystemTreeListFailed(error: String) {

    }
}