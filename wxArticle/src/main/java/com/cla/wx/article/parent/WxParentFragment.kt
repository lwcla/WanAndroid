package com.cla.wx.article.parent

import com.cla.wx.article.detail.WxDetailFragment
import com.konsung.basic.bean.TwoBean
import com.konsung.basic.bean.project.ProjectTitleBean
import com.konsung.basic.presenter.LoadWxArticleTitle
import com.konsung.basic.presenter.LoadWxArticleTitleView
import com.konsung.basic.ui.fragment.BasicFragment
import com.konsung.basic.presenter.BasicPresenter
import com.konsung.basic.ui.fragment.VpBasicFragment

/**
 * 公众号
 */
class WxParentFragment : VpBasicFragment() {

    private val loadWxArticleTitle by lazy { initLoadWxArticleTitle() }

    override fun initPresenters(): List<BasicPresenter>? = listOf(loadWxArticleTitle)

    override fun initView() {

    }

    override fun initEvent() {

    }

    override fun initData() {
        resetData()
    }

    private fun addFragment(t: List<ProjectTitleBean>) {

        val list = mutableListOf<TwoBean<String, BasicFragment>>()

        if (t.size > 1) {
            for (i in 0 until t.size) {
                val bean = t[i]
                val wxDetailFragment = WxDetailFragment()
                wxDetailFragment.cId = bean.id

                val fragment = TwoBean<String, BasicFragment>(bean.name, wxDetailFragment)
                list.add(fragment)
            }
        }

        initViewPager(list)
    }

    private fun initLoadWxArticleTitle(): LoadWxArticleTitle {

        val view = object : LoadWxArticleTitleView() {

            override fun success(t: List<ProjectTitleBean>, refreshData: Boolean) {
                addFragment(t)
            }
        }

        return LoadWxArticleTitle(this, view)
    }

    override fun resetData() {
        loadWxArticleTitle.load()
    }
}