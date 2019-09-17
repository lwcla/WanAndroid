package com.cla.system.tree.list

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.cla.system.tree.R
import com.cla.system.tree.view.TreeListContentView
import com.konsung.basic.bean.tree.SystemTreeListBean
import com.konsung.basic.ui.HeightView
import com.konsung.basic.util.Debug
import com.konsung.basic.util.StringUtils
import com.konsung.basic.util.Utils
import com.konsung.basic.util.toast

class SystemTreeListAdapter(private val dataList: MutableList<SystemTreeListBean>) : RecyclerView.Adapter<SystemTreeListAdapter.SystemTreeListViewHolder>() {

    companion object {
        val TAG: String = SystemTreeListAdapter::class.java.simpleName
    }

    var clickListener: View.OnClickListener? = null

    fun update(list: List<SystemTreeListBean>) {

        if (dataList.size > 0) {
            for (bean in dataList) {

                if (!bean.isOpen) {
                    continue
                }

                for (it in list) {
                    if (it.name == bean.name) {
                        it.isOpen = bean.isOpen
                        continue
                    }
                }
            }
        }

        dataList.clear()
        dataList.addAll(list.filter { it.name.isNotEmpty() })
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SystemTreeListViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_system_tree_list_adapter, parent, false)
        return SystemTreeListViewHolder(context, view, clickListener)
    }

    override fun onBindViewHolder(holder: SystemTreeListViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    class SystemTreeListViewHolder(private val context: Context, itemView: View, listener: View.OnClickListener?) : RecyclerView.ViewHolder(itemView) {

        private val cardView = itemView.findViewById<CardView>(R.id.cardView)
        private val tvName = itemView.findViewById<TextView>(R.id.tvName)
        private val rlDetail = itemView.findViewById<RelativeLayout>(R.id.rlDetail)
        private val tvDetail = itemView.findViewById<TextView>(R.id.tvDetail)
        private val llTreeContent = itemView.findViewById<TreeListContentView>(R.id.llTreeContent)
        private val llContent = itemView.findViewById<RelativeLayout>(R.id.llContent)
        private val heightView by lazy { HeightView(llTreeContent) }

        private var bean: SystemTreeListBean? = null
        private var animatorSet: AnimatorSet? = null
        private var contentHeight = 0
        //初始的treeContent高度
        private var originalHeight = context.resources.getDimension(R.dimen.dp_34).toInt()
        private var rlDetailHeight = originalHeight
        private var singleLine = true

        init {

            itemView.setOnClickListener {
                it.setTag(R.id.recycler_view_adapter_item_click, bean)
                listener?.onClick(it)
            }

            rlDetail.setOnClickListener {

                bean?.apply {
                    val open = isOpen
                    isOpen = !open
                    open(!open)
                }
            }

            llTreeContent.itemClickListener = View.OnClickListener {

                val bean: SystemTreeListBean? = it.getTag(R.id.text_view_click_data) as? SystemTreeListBean
                        ?: return@OnClickListener

                val position = it.getTag(R.id.text_view_click_position) as? Int
                if (position == null || position < 0) {
                    context.toast(TAG, R.string.click_position_is_error)
                    return@OnClickListener
                }

                context.toast(TAG, "点击${bean!!.name} $position")
            }

            StringUtils.instance.loadTextIcon(context, tvDetail)
            tvDetail.setText(R.string.icon_on_the_details)

            val layoutParams = llTreeContent.layoutParams as RelativeLayout.LayoutParams
            val layoutParams1 = llContent.layoutParams as FrameLayout.LayoutParams


            val treeViewWidth = (Utils.getScreenProperty(context).a
                    - cardView.contentPaddingLeft - cardView.contentPaddingRight
                    - llContent.paddingStart - llContent.paddingEnd - layoutParams1.marginStart - layoutParams1.marginEnd
                    - layoutParams.marginStart - layoutParams.marginEnd)

            llTreeContent.contentWidth = treeViewWidth

            rlDetail.layoutParams.height = rlDetailHeight
            Debug.info(TAG, "SystemTreeListViewHolder open treeViewWidth=$treeViewWidth originalHeight=$originalHeight")
        }

        fun open(toOpen: Boolean) {

            if (singleLine) {
                return
            }

            animatorSet?.cancel()
            animatorSet = AnimatorSet()

            Debug.info(TAG, "SystemTreeListViewHolder bind contentHeight=$contentHeight originalHeight=$originalHeight")

            val startHeight: Int
            val endHeight: Int

            val startRotation: Float
            val endRotation: Float

            if (toOpen) {
                startHeight = originalHeight
                endHeight = contentHeight

                startRotation = 0f
                endRotation = 180f
            } else {
                startHeight = contentHeight
                endHeight = originalHeight

                startRotation = 180f
                endRotation = 360f
            }

            val hAnimator = ObjectAnimator.ofInt(heightView, "viewHeight", startHeight, endHeight)
            val rAnimator = ObjectAnimator.ofFloat(tvDetail, "rotation", startRotation, endRotation)

            animatorSet?.apply {
                play(hAnimator)?.with(rAnimator)
                interpolator = DecelerateInterpolator()
                duration = 200
                start()
            }
        }

        fun bind(bean: SystemTreeListBean) {

            this.bean = bean
            tvName.text = StringUtils.instance.formHtml(bean.name)

            llTreeContent.update(bean)

            val two = llTreeContent.getViewHeight()
            //加上rlDetailHeight,避免展开之后还会遮挡文字
            contentHeight = two.a + rlDetailHeight

//            val params = llTreeContent.layoutParams as LinearLayout.LayoutParams
            if (two.b == 1) {
                singleLine = true
                rlDetail.visibility = View.GONE
//                params.bottomMargin = marginBottom
                heightView.viewHeight = originalHeight
            } else {
                singleLine = false
                rlDetail.visibility = View.VISIBLE
//                params.bottomMargin = 0

                if (bean.isOpen) {
                    heightView.viewHeight = contentHeight
                    tvDetail.rotation = 180f
                } else {
                    heightView.viewHeight = originalHeight
                    tvDetail.rotation = 360f
                }
            }
        }
    }
}