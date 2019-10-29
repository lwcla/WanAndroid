package com.cla.project.tree.parent

import com.cla.project.tree.R
import com.cla.project.tree.fragment.NewestProjectFragment
import com.cla.project.tree.fragment.ProjectTreeFragment
import com.konsung.basic.bean.TwoBean
import com.konsung.basic.bean.project.ProjectTitleBean
import com.konsung.basic.ui.fragment.BasicFragment
import com.konsung.basic.presenter.BasicPresenter
import com.konsung.basic.ui.fragment.VpBasicFragment

/**
 * 项目分类
 */
class ProjectParentFragment : VpBasicFragment() {

    private val projectTitlePresenter by lazy { initProjectTitlePresenter() }

    override fun initPresenters(): List<BasicPresenter>? = listOf(projectTitlePresenter)

    override fun initView() {
        projectTitlePresenter.load()
    }

    override fun initEvent() {

    }

    override fun initData() {

    }

    private fun addFragment(t: List<ProjectTitleBean>) {

        val newestProjectFragment = NewestProjectFragment()

        val list = mutableListOf<TwoBean<String, BasicFragment>>()
        val f1 = TwoBean<String, BasicFragment>(context!!.getString(R.string.newest_project), newestProjectFragment)
        list.add(f1)

        if (t.isNotEmpty()) {
            for (i in 0 until t.size) {
                val bean = t[i]
                val  projectTreeFragment = ProjectTreeFragment()
                projectTreeFragment.cId = bean.id

                val fragment = TwoBean<String, BasicFragment>(bean.name, projectTreeFragment)
                list.add(fragment)
            }
        }

        initViewPager(list)
    }

    private fun initProjectTitlePresenter(): LoadProjectTitle {

        val view = object : LoadProjectTitleView() {

            override fun success(t: List<ProjectTitleBean>, refreshData: Boolean) {
                addFragment(t)
            }
        }

        return LoadProjectTitle(this, view)
    }

    override fun resetData() {
        projectTitlePresenter.load()
    }

}