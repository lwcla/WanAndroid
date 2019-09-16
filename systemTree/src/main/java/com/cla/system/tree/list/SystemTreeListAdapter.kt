package com.cla.system.tree.list

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cla.system.tree.R
import com.cla.system.tree.view.TreeListContentView
import com.konsung.basic.bean.tree.SystemTreeListBean
import com.konsung.basic.ui.HeightView
import com.konsung.basic.util.Debug
import com.konsung.basic.util.StringUtils

class SystemTreeListAdapter(private val dataList: MutableList<SystemTreeListBean>) : RecyclerView.Adapter<SystemTreeListAdapter.SystemTreeListViewHolder>() {

    companion object {
        val TAG: String = SystemTreeListAdapter::class.java.simpleName
    }

    private val pool: RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()
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
        return SystemTreeListViewHolder(context, view, clickListener, pool)
    }

    override fun onBindViewHolder(holder: SystemTreeListViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    class SystemTreeListViewHolder(private val context: Context, itemView: View, listener: View.OnClickListener?, pool: RecyclerView.RecycledViewPool) : RecyclerView.ViewHolder(itemView) {

        private val tvName = itemView.findViewById<TextView>(R.id.tvName)
        private val rlDetail = itemView.findViewById<RelativeLayout>(R.id.rlDetail)
        private val tvDetail = itemView.findViewById<TextView>(R.id.tvDetail)
        private val llTreeContent = itemView.findViewById<TreeListContentView>(R.id.llTreeContent)
        private val heightView by lazy { HeightView(llTreeContent) }

        private var bean: SystemTreeListBean? = null
        private var animatorSet: AnimatorSet? = null

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

            StringUtils.instance.loadTextIcon(context, tvDetail)
            tvDetail.setText(R.string.icon_on_the_details)

        }

        fun open(toOpen: Boolean) {

            animatorSet?.cancel()
            animatorSet = AnimatorSet()

            var height = llTreeContent.getViewHeight()
            Debug.info(TAG, "SystemTreeListViewHolder open height=$height")

            if (height == 0) {
                height = llTreeContent.height
            }

            if (height == 0) {
                height = 100
            }

            val startHeight: Int
            val endHeight: Int

            val startRotation: Float
            val endRotation: Float

            if (toOpen) {
                startHeight = 0
                endHeight = height

                startRotation = 0f
                endRotation = 180f
            } else {
                startHeight = height
                endHeight = 0

                startRotation = 180f
                endRotation = 360f
            }

            val hAnimator = ObjectAnimator.ofInt(heightView, "viewHeight", startHeight, endHeight)
            val rAnimator = ObjectAnimator.ofFloat(tvDetail, "rotation", startRotation, endRotation)

            animatorSet?.apply {
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator?) {
                        llTreeContent.visibility = View.VISIBLE
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        if (!toOpen) {
                            llTreeContent.visibility = View.GONE
                        } else {
                            llTreeContent.visibility = View.VISIBLE
                        }
                    }
                })

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

            if (bean.isOpen) {
                llTreeContent.visibility = View.VISIBLE
                tvDetail.rotation = 180f
            } else {
                llTreeContent.visibility = View.GONE
                tvDetail.rotation = 360f
            }
        }
    }
}