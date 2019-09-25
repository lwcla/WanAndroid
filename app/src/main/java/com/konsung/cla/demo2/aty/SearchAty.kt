package com.konsung.cla.demo2.aty

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.transition.Explode
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration
import com.konsung.basic.bean.search.SearchKey
import com.konsung.basic.ui.BasicAty
import com.konsung.basic.ui.BasicPresenter
import com.konsung.basic.util.ConvertUtils
import com.konsung.basic.util.StringUtils
import com.konsung.basic.util.toast
import com.konsung.cla.demo2.App
import com.konsung.cla.demo2.R
import com.konsung.cla.demo2.adapter.SearchKeyAdapter
import com.konsung.cla.demo2.dialog.ChooseWxArticleNameListener
import com.konsung.cla.demo2.dialog.ChooseWxSearchDialog
import com.konsung.cla.demo2.presenter.SearchHistoryPresenter
import com.konsung.cla.demo2.presenter.SearchHistoryView
import com.konsung.cla.demo2.presenter.SearchHotPresenter
import com.konsung.cla.demo2.presenter.SearchHotView
import kotlinx.android.synthetic.main.activity_search.*

/**
 * 搜索页面
 */
class SearchAty : BasicAty(), View.OnClickListener {

    companion object {
        val TAG: String = SearchAty::class.java.simpleName
        const val INIT_DETAIL = 300L
    }

    init {
        initDelay = INIT_DETAIL
    }

    private val tvWxArticleName by lazy { findViewById<TextView>(R.id.tvWxName) }
    private val cvWxArticleName by lazy { findViewById<CardView>(R.id.cvWxName) }
    private val tvWxTitle1 by lazy { findViewById<TextView>(R.id.tvWxTitle1) }
    private val tvWxTitle2 by lazy { findViewById<TextView>(R.id.tvWxTitle2) }
    private val switch by lazy { findViewById<SwitchCompat>(R.id.switchSearch) }

    private val searchHotPresenter by lazy { initSearchHotPresenter() }
    private val historyKeyPresenter by lazy { initHistoryKeyPresenter() }

    private var hotSearchAdapter: SearchKeyAdapter? = null
    private var historyKeyAdapter: SearchKeyAdapter? = null

    private var wxName: String? = null
    private var wxId: Int? = null
    private val showWxInfo = Runnable {
        showSearchWx(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.enterTransition = Explode().setDuration(INIT_DETAIL)
        window.exitTransition = Explode().setDuration(INIT_DETAIL)
    }

    override fun onResume() {
        super.onResume()

        if (historyKeyAdapter != null) {
            historyKeyPresenter.load()
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_search

    override fun initPresenter(): List<BasicPresenter>? = listOf(searchHotPresenter, historyKeyPresenter)

    override fun initView() {
        StringUtils.instance.loadTextIcon(context, tvBack)
        StringUtils.instance.loadTextIcon(context, tvClear)
        StringUtils.instance.loadTextIcon(context, tvDelete)

        showSearchWx(false)
    }

    override fun initEvent() {
        tvBack.setOnClickListener(this)
        tvSearch.setOnClickListener(this)
        tvClear.setOnClickListener(this)
        tvDelete.setOnClickListener(this)
        tvWxArticleName.setOnClickListener(this)

        switch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                showChooseWxDialog()
//                myHandler.postDelayed(showWxInfo, 1000)
                showSearchWx(true)
            } else {
                showSearchWx(false)
            }
        }

        etSearch.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if (s.toString().isEmpty()) {
                    tvClear.visibility = View.GONE
                } else {
                    tvClear.visibility = View.VISIBLE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        etSearch.setOnEditorActionListener(object : TextView.OnEditorActionListener {

            override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
                /*判断是否是“搜索”键*/
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val key = etSearch.text.toString().trim()
                    if (TextUtils.isEmpty(key)) {
                        toast(TAG, R.string.key_is_null)
                        return true
                    }
                    searchKey(key)
                    hideSoftKeyboard(this@SearchAty)
                    return true
                }
                return false
            }
        })

    }

    override fun initData() {
        searchHotPresenter.load()
        historyKeyPresenter.load()
    }

    private fun setHotKey(t: List<SearchKey>) {
        if (hotSearchAdapter == null) {
            hotSearchAdapter = initRv(rvHot, t, true)
        } else {
            hotSearchAdapter?.setNewData(t)
        }
    }

    private fun setHistoryKey(t: List<SearchKey>) {

        if (historyKeyAdapter == null) {
            historyKeyAdapter = initRv(rvHistory, t, false)
        } else {
            historyKeyAdapter?.setNewData(t)
        }
    }

    private fun initRv(rv: RecyclerView, t: List<SearchKey>, isHotKey: Boolean): SearchKeyAdapter {

        val searchAdapter = SearchKeyAdapter(this, t, isHotKey)
        searchAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.tvName -> {

                    if (position < 0) {
                        return@setOnItemChildClickListener
                    }

                    if (position >= adapter.data.size) {
                        return@setOnItemChildClickListener
                    }

                    val key = adapter.data[position] as SearchKey
                    val name = key.name.toString()
                    etSearch.setText(name)
                    etSearch.setSelection(name.length)
                    searchKey(key)
                }
            }
        }

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

        val horizontalSpacing = ConvertUtils.dp2px(context, 25.toFloat())
        val verticalSpacing = ConvertUtils.dp2px(context, 15.toFloat())
        rv.addItemDecoration(SpacingItemDecoration(horizontalSpacing, verticalSpacing))
        rv.layoutManager = chipsLayoutManager
        rv.adapter = searchAdapter

        return searchAdapter
    }

    private fun searchKey(key: String) {
        val searchKey = SearchKey()
        searchKey.name = key
        searchKey(searchKey)
    }

    private fun searchKey(searchKey: SearchKey) {

        val key = searchKey.name
        historyKeyPresenter.save(searchKey)

        var name = wxName
        var id = wxId
        if (!switch.isChecked) {
            name = ""
            id = -1
        }

        App.productUtils.startSearchResultAty(this, key, switch.isChecked, name, id)
    }

    private fun showChooseWxDialog() {

        val chooseWxSearchDialog = ChooseWxSearchDialog()
        chooseWxSearchDialog.apply {
            chooseWxArticleNameListener = object : ChooseWxArticleNameListener {
                override fun choose(name: String?, id: Int?) {

                    //这次弹窗期间没有做任何选择
                    if (name.isNullOrEmpty()) {
                        //之前也没有选择过
                        if (tvWxArticleName.text == getString(R.string.unknown)) {
                            switch.isChecked = false
                        }
                        return
                    }

                    tvWxArticleName.text = name
                    wxName = name
                    wxId = id
                    showSearchWx(true)
                }
            }

            if (!isAdded) {
                show(supportFragmentManager, ChooseWxSearchDialog::class.java.simpleName)
            }
        }
    }

    /**
     * 是否显示搜索具体是谁的微信公众号的描述
     */
    private fun showSearchWx(show: Boolean) {
        myHandler.removeCallbacks(showWxInfo)
        if (show) {
            cvWxArticleName.visibility = View.VISIBLE
            tvWxTitle1.visibility = View.VISIBLE
            tvWxTitle2.visibility = View.VISIBLE

            tvWxTitle1.text = getString(R.string.search)
            etSearch.hint = getString(R.string.search_wx_article)
        } else {
            cvWxArticleName.visibility = View.GONE
            tvWxTitle1.visibility = View.VISIBLE
            tvWxTitle2.visibility = View.GONE

            tvWxTitle1.text = getString(R.string.search_wx_article)
            etSearch.hint = getString(R.string.search_all)
        }
    }

    private fun initHistoryKeyPresenter(): SearchHistoryPresenter {

        val view = object : SearchHistoryView() {

            override fun success(t: List<SearchKey>, refreshData: Boolean) {
                setHistoryKey(t)
            }
        }

        return SearchHistoryPresenter(this, view)
    }

    private fun initSearchHotPresenter(): SearchHotPresenter {

        val view = object : SearchHotView() {

            override fun success(t: List<SearchKey>, refreshData: Boolean) {
                setHotKey(t)
            }
        }

        return SearchHotPresenter(this, view)
    }

    override fun onClick(v: View) {
        when (v.id) {

            R.id.tvBack -> finish()

            R.id.tvSearch -> {
                val key = etSearch.text.toString()
                if (key.isEmpty()) {
                    toast(TAG, R.string.search_content_is_empty)
                    return
                }

                searchKey(key)
            }

            R.id.tvClear -> etSearch.setText("")

            R.id.tvDelete -> historyKeyPresenter.clear()

            R.id.tvWxName -> showChooseWxDialog()
        }
    }
}
