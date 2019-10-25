package com.konsung.cla.demo2.aty

import com.chad.library.adapter.base.BaseViewHolder
import com.konsung.basic.adapter.BasicDataQuickAdapter
import com.konsung.basic.bean.HomeData
import com.konsung.basic.config.BaseConfig
import com.konsung.basic.presenter.BasicPresenter
import com.konsung.basic.presenter.Presenter
import com.konsung.basic.ui.HomeDataAty
import com.konsung.basic.util.Debug
import com.konsung.basic.util.toast
import com.konsung.cla.demo2.R
import com.konsung.cla.demo2.adapter.SearchResultAdapter
import com.konsung.cla.demo2.presenter.SearchResultPresenter

class SearchResultAty : HomeDataAty() {
    companion object {

        val TAG: String = SearchResultAty::class.java.simpleName
    }

    private val searchResultPresenter by lazy { SearchResultPresenter(this, homeView, getKey(), getWxId()) }

    private var key: String? = null

    private var searchForWxArticle = false
    private var wxId = -1
    override fun initPresenter(): List<BasicPresenter>? = listOf(searchResultPresenter, collectPresenter)

    override fun initPresenterList(): List<Presenter>? = null

    private fun getKey(): String? {

        if (key.isNullOrEmpty()) {
            key = intent.getStringExtra(BaseConfig.SEARCH_KEY)
        }

        return key
    }

    private fun getWxId(): Int {
        searchForWxArticle = intent.getBooleanExtra(BaseConfig.SEARCH_FOR_WX_ARTICLE, false)
        if (searchForWxArticle) {
            if (wxId < 0) {
                wxId = intent.getIntExtra(BaseConfig.SEARCH_FOR_WX_ARTICLE_ID, -1)
            }
        }

        Debug.info(TAG, "getWxId id=$wxId")
        return wxId
    }

    override fun getAtyTitle(): String {
        var title = getKey()
        if (title.isNullOrEmpty()) {
            toast(TAG, R.string.key_is_null)
            finish()
            return ""
        }

        val wxName = intent.getStringExtra(BaseConfig.SEARCH_FOR_WX_ARTICLE_NAME)
        if (searchForWxArticle) {
            if (wxId < 0) {
                toast(TAG, R.string.wx_id_is_null)
                finish()
                return ""
            }

            title = "$wxName : $title"
        }

        return title
    }

    override fun getImvStartId(): Int = R.id.imvStart

    override fun initAdapter(t: List<HomeData.DatasBean>): BasicDataQuickAdapter<BaseViewHolder> = SearchResultAdapter(context, t)

    override fun loadMoreData() {
        searchResultPresenter.loadMore()
    }

    override fun resetData() {
        searchResultPresenter.refresh()
    }
}
