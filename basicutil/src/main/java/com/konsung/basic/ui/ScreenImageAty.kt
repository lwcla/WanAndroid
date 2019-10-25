package com.konsung.basic.ui

import android.content.Context
import android.os.Bundle
import android.transition.Fade
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.konsung.basic.bean.ScreenImageData
import com.konsung.basic.config.BaseConfig
import com.konsung.basic.presenter.BasicPresenter
import com.konsung.basic.presenter.Presenter
import com.konsung.basic.util.R
import com.konsung.basic.util.toast
import kotlinx.android.synthetic.main.activity_screen_image.*

class ScreenImageAty : BasicAty() {
    companion object {

        const val INIT_DELAY = 500L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        //设置全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)

        window.enterTransition = Fade().setDuration(INIT_DELAY)
        window.exitTransition = Fade().setDuration(INIT_DELAY)
    }

    override fun getLayoutId(): Int = R.layout.activity_screen_image

    override fun initPresenter(): List<BasicPresenter>? = null

    override fun initPresenterList(): List<Presenter>? = null

    override fun initView() {
    }

    override fun initEvent() {
    }

    override fun initData() {
        val data: ScreenImageData? = intent?.getSerializableExtra(BaseConfig.SCREEN_IAMGE_DATA) as? ScreenImageData
        if (data == null) {
            toast(TAG, R.string.data_error)
            finish()
            return
        }

        val adapter = ScreenImageAdapter(this, data.urls, View.OnClickListener { finishAfterTransition() })
        adapter.longClickListener = View.OnLongClickListener { true }

        viewPager.adapter = adapter

        val position = data.currentIndex
        viewPager.currentItem = position
    }
}

class ScreenImageAdapter(private val context: Context, private val urlList: List<String>, private val listener: View.OnClickListener) : PagerAdapter() {

    var longClickListener: View.OnLongClickListener? = null

    private val options: RequestOptions by lazy { RequestOptions().fitCenter().placeholder(R.mipmap.pic_nohistoricaldata).error(R.mipmap.pic_loading_fail) }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int = urlList.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val imageView = ImageView(context)
        imageView.layoutParams = ViewPager.LayoutParams()
        imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
        imageView.setOnClickListener(listener)
        imageView.setOnLongClickListener(longClickListener)

        val path = urlList[position]
        Glide.with(imageView).load(path).apply(options).into(imageView)

        container.addView(imageView)
        return imageView
    }
}
