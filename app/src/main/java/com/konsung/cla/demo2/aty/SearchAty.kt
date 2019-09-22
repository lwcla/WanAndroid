package com.konsung.cla.demo2.aty

import android.os.Bundle
import android.transition.Explode
import android.view.Gravity
import android.view.View
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration
import com.konsung.basic.bean.search.SearchHotKey
import com.konsung.basic.ui.BasicAty
import com.konsung.basic.ui.BasicPresenter
import com.konsung.basic.util.ConvertUtils
import com.konsung.basic.util.StringUtils
import com.konsung.cla.demo2.R
import com.konsung.cla.demo2.adapter.HotSearchAdapter
import com.konsung.cla.demo2.presenter.SearchHotPresenter
import com.konsung.cla.demo2.presenter.SearchHotView
import kotlinx.android.synthetic.main.activity_search.*

class SearchAty : BasicAty(), View.OnClickListener {


    companion object {
        val TAG: String = SearchAty::class.java.simpleName
        const val INIT_DETAIL = 300L
    }

    init {
        initDelay = INIT_DETAIL
    }

    private val searchHotPresenter by lazy { initSearchHotPresenter() }
    private var hotSearchAdapter: HotSearchAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.enterTransition = Explode().setDuration(INIT_DETAIL)
        window.exitTransition = Explode().setDuration(INIT_DETAIL)
    }

    override fun getLayoutId(): Int = R.layout.activity_search

    override fun initPresenter(): List<BasicPresenter>? = listOf(searchHotPresenter)

    override fun initView() {
        StringUtils.instance.loadTextIcon(context, tvBack)
    }

    override fun initEvent() {
        tvBack.setOnClickListener(this)
        tvSearch.setOnClickListener(this)

    }

    override fun initData() {
        searchHotPresenter.load()
    }

    private fun setHotKey(t: List<SearchHotKey>) {

        if (hotSearchAdapter == null) {

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

            rvHot.layoutManager = chipsLayoutManager

            val horizontalSpacing = ConvertUtils.dp2px(context, 25.toFloat())
            val verticalSpacing = ConvertUtils.dp2px(context, 15.toFloat())
            rvHot.addItemDecoration(SpacingItemDecoration(horizontalSpacing, verticalSpacing))

            hotSearchAdapter = HotSearchAdapter(t)
            rvHot.adapter = hotSearchAdapter

        } else {
            hotSearchAdapter?.setNewData(t)
        }
    }

    private fun initSearchHotPresenter(): SearchHotPresenter {

        val view = object : SearchHotView() {

            override fun success(t: List<SearchHotKey>, refreshData: Boolean) {
                setHotKey(t)
            }
        }

        return SearchHotPresenter(this, view)
    }

    override fun onClick(v: View) {
        when (v.id) {

            R.id.tvBack -> finish()

            R.id.tvSearch -> {

            }

        }
    }
}
