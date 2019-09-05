package com.konsung.cla.demo2.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.konsung.basic.bean.ThreeBean
import com.konsung.basic.bean.TwoBean
import com.konsung.basic.util.AppUtils
import com.konsung.basic.util.StringUtils
import com.konsung.basic.util.toast
import com.konsung.cla.demo2.App.Companion.context
import com.konsung.cla.demo2.R


class ShareDialog : BottomSheetDialogFragment(), View.OnClickListener {

    companion object {
        val TAG: String = ShareDialog::class.java.simpleName
    }

    private var rootView: View? = null

    private var link: String? = null
    var shareDialogListener: ShareDialogListener? = null
    var collectFlag = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        context?.let {
            (rootView?.parent as? View)?.setBackgroundColor(ContextCompat.getColor(it, android.R.color.transparent))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.dialog_share, container, false)
        initAdapter1(rootView!!)
        initAdapter2(rootView!!)

        val tvCancel = rootView!!.findViewById<TextView>(R.id.tvCancel)
        tvCancel.setOnClickListener {
            dismissAllowingStateLoss()
        }

        return rootView
    }

    private fun initAdapter1(view: View) {
        val rvShare1 = view.findViewById<RecyclerView>(R.id.rvShare1)
        val qq = ThreeBean(R.string.icon_qq, R.string.qq, R.color.qq_color)
        val qqSpace = ThreeBean(R.string.icon_qq_space, R.string.qq_space, R.color.qq_space_color)
        val weChat = ThreeBean(R.string.icon_we_chat, R.string.we_chat, R.color.qq_we_char_color)
        val friend = ThreeBean(R.string.icon_circle_of_friends, R.string.circle_of_friends, R.color.qq_we_char_color)
        val other = ThreeBean(R.string.icon_other, R.string.other, R.color.normal_color1)
        val list1 = listOf(qq, qqSpace, weChat, friend, other)

        val adapter1 = ShareAdapter(list1)
        adapter1.listener = this
        rvShare1.layoutManager = LinearLayoutManager(context!!, RecyclerView.HORIZONTAL, false)
        rvShare1.adapter = adapter1
    }

    private fun initAdapter2(view: View) {
        val rvShare2 = view.findViewById<RecyclerView>(R.id.rvShare2)

        val collectColor: Int = if (collectFlag) {
            R.color.qq_space_color
        } else {
            R.color.normal_color1
        }

        val collectText: Int = if (collectFlag) {
            R.string.cancel_collect
        } else {
            R.string.collect
        }

        val collect = ThreeBean(R.string.icon_collect, collectText, collectColor)
        val link = ThreeBean(R.string.icon_link, R.string.copy_link, R.color.normal_color1)
        val browser = ThreeBean(R.string.icon_browser, R.string.open_in_browser, R.color.normal_color1)
        val list2 = listOf(collect, link, browser)

        val adapter2 = ShareAdapter(list2)
        adapter2.listener = this
        rvShare2.layoutManager = GridLayoutManager(context!!, 4)
        rvShare2.adapter = adapter2
    }

    override fun onClick(v: View) {
        val iconRes = v.getTag(R.id.recycler_view_adapter_item_click) as? Int
        iconRes?.let {
            when (it) {
                R.string.icon_qq -> toast(TAG, "qq")
                R.string.icon_qq_space -> toast(TAG, "qq空间")
                R.string.icon_we_chat -> toast(TAG, "微信")
                R.string.icon_circle_of_friends -> toast(TAG, "朋友圈")

                R.string.icon_other -> {
                    AppUtils.instance.shareToSystem(context, link)
                }

                //收藏
                R.string.icon_collect -> {
                    collectFlag = !collectFlag
                    shareDialogListener?.collect()
                }

                R.string.icon_link -> {
                    AppUtils.instance.copyToClip(context, link)
                }

                R.string.icon_browser -> {
                    AppUtils.instance.openByBrowser(context, link)
                }

                else -> {

                }
            }
        }

        dismissAllowingStateLoss()
    }

    fun show(manager: FragmentManager, tag: String?, link: String) {
        this.link = link
        show(manager, tag)
    }
}

interface ShareDialogListener {
    fun collect()
}

class ShareAdapter(private val list: List<ThreeBean<Int, Int, Int>>) : RecyclerView.Adapter<ShareAdapter.ShareViewHolder>() {

    var listener: View.OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShareViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.button_share, parent, false)
        return ShareViewHolder(context, view, listener)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ShareViewHolder, position: Int) {
        holder.bind(list[position])
    }


    class ShareViewHolder @SuppressLint("ClickableViewAccessibility") constructor(context: Context, view: View, listener: View.OnClickListener?) : RecyclerView.ViewHolder(view) {

        companion object {
            val TAG: String = ShareViewHolder::class.java.simpleName
        }

        private val tvIcon: TextView = view.findViewById(R.id.tvIcon)
        private val tvName: TextView = view.findViewById(R.id.tvName)
        private var two: TwoBean<Int, Int>? = null


        init {
            StringUtils.instance.loadTextIcon(context, tvIcon)

            view.setOnClickListener { v ->
                two?.let {
                    v.setTag(R.id.recycler_view_adapter_item_click, it.a)
                    listener?.onClick(v)
                }
            }

            view.setOnTouchListener { v, ev ->
                when (ev.action) {
                    MotionEvent.ACTION_DOWN -> tvIcon.setBackgroundResource(R.drawable.bg_grey_round)
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> tvIcon.setBackgroundResource(R.drawable.bg_white_round)
                }

                return@setOnTouchListener false
            }
        }

        fun bind(three: ThreeBean<Int, Int, Int>) {
            tvIcon.setText(three.a)
            tvName.setText(three.b)
            tvIcon.setTextColor(ContextCompat.getColor(context, three.c))

        }
    }
}
