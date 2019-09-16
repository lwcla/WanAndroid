package com.cla.system.tree.list

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.cla.system.tree.R
import com.konsung.basic.bean.tree.SystemTreeListBean
import com.konsung.basic.ui.BasicFragment
import com.konsung.basic.ui.BasicPresenter
import com.konsung.basic.ui.RefreshRecyclerView
import com.konsung.basic.ui.SpaceDecoration
import com.konsung.basic.util.toast

/**
 * 体系分类列表
 */
class SystemTreeListFragment : BasicFragment() {

    private val refreshRv by lazy { showView?.findViewById<RefreshRecyclerView>(R.id.refreshRv) }

    private val loadTreeList: LoadSystemTreeList by lazy { initLoadSystemTreeList() }
    private val systemAdapter = SystemTreeListAdapter(mutableListOf())

    override fun getLayoutId(): Int = R.layout.fragment_system_tree_list

    override fun initPresenters(): List<BasicPresenter>? = listOf(loadTreeList)

    override fun initView() {
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

            val bean = it.getTag(R.id.recycler_view_adapter_item_click) as? SystemTreeListBean
                    ?: return@OnClickListener

            toast(TAG, "点击 ${bean.name}")
        }
    }

    override fun initData() {
        resetData()
    }

    private fun initLoadSystemTreeList(): LoadSystemTreeList {

        val view = object : LoadSystemTreeListView() {

            override fun success(t: List<SystemTreeListBean>, refreshData: Boolean) {
                systemAdapter.update(t)
                refreshRv?.finishRefresh()
            }
        }

        return LoadSystemTreeList(this, view)
    }

    override fun resetData() {
        loadTreeList.refresh()
    }

}