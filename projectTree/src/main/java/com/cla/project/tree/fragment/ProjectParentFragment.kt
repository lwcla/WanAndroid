package com.cla.project.tree.fragment

import com.cla.project.tree.presenter.LoadProjectTitle
import com.cla.project.tree.presenter.LoadProjectTitleView
import com.konsung.basic.bean.TwoBean
import com.konsung.basic.bean.project.ProjectTitleBean
import com.konsung.basic.ui.BasicFragment
import com.konsung.basic.ui.BasicPresenter
import com.konsung.basic.ui.VpBasicFragment

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
        val f1 = TwoBean<String, BasicFragment>(t[0].name, newestProjectFragment)
        list.add(f1)

        if (t.size > 1) {
            for (i in 1 until t.size) {
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