package com.konsung.cla.demo2.aty

import android.os.Bundle
import android.transition.Explode
import com.konsung.basic.ui.BasicAty
import com.konsung.basic.ui.BasicPresenter
import com.konsung.cla.demo2.R
import kotlinx.android.synthetic.main.activity_to_do.*

/**
 * todo页面
 */
class ToDoAty : BasicAty() {

    companion object {
        val TAG: String = ToDoAty::class.java.simpleName
        const val INIT_DETAIL = 300L
    }

    init {
        initDelay = INIT_DETAIL
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.enterTransition = Explode().setDuration(INIT_DETAIL)
        window.exitTransition = Explode().setDuration(INIT_DETAIL)
    }

    override fun getLayoutId(): Int = R.layout.activity_to_do

    override fun initPresenter(): List<BasicPresenter>? = null

    override fun initView() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun initEvent() {
        toolbar.setNavigationOnClickListener {
            finishAfterTransition()
        }
    }

    override fun initData() {

    }

}