package com.cla.system.tree.list

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.cla.system.tree.R
import com.cla.system.tree.list.adapter.SystemTreeListAdapter
import com.konsung.basic.bean.tree.SystemTreeListBean
import com.konsung.basic.bean.tree.SystemTreeTitle
import com.konsung.basic.ui.BasicFragment
import com.konsung.basic.ui.BasicPresenter
import com.konsung.basic.ui.SpaceDecoration
import com.konsung.basic.util.AppUtils

/**
 * 体系分类列表
 */
class SystemTreeListFragment : BasicFragment() {

    private val loadTreeList: LoadSystemTreeList by lazy { initLoadSystemTreeList() }
    private val systemAdapter = SystemTreeListAdapter(mutableListOf())

    override fun getLayoutId(): Int = R.layout.fragment_system_tree_list

    override fun initPresenters(): List<BasicPresenter>? = listOf(loadTreeList)

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

    override fun refreshView() {
        refreshRv?.refreshDataAfterScrollTop()
    }

}