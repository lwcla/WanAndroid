package com.cla.navigation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cla.navigation.R
import com.konsung.basic.bean.HomeData
import com.konsung.basic.util.StringUtils

class NavigationAdapter2(private val dataList: MutableList<HomeData.DatasBean>) : RecyclerView.Adapter<NavigationAdapter2.NavigationViewHolder2>() {

    var clickListener: View.OnClickListener? = null

    fun refreshData(list: List<HomeData.DatasBean>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavigationViewHolder2 {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.adapter2_navigation, parent, false)
        return NavigationViewHolder2(view, clickListener)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: NavigationViewHolder2, position: Int) {
        holder.bind(dataList[position])
    }

    class NavigationViewHolder2(itemView: View, listener: View.OnClickListener?) : RecyclerView.ViewHolder(itemView) {

        private val tvName by lazy { itemView.findViewById<TextView>(R.id.tvName) }
        private var bean: HomeData.DatasBean? = null

        init {
            tvName.setOnClickListener {

                if (bean == null) {
                    return@setOnClickListener
                }

                it.setTag(R.id.recycler_view_adapter_item_click, bean)
                listener?.onClick(it)
            }

            tvName.setTextColor(StringUtils.instance.intRandomColor())
        }

        fun bind(bean: HomeData.DatasBean) {
            this.bean = bean
            tvName.text = bean.title
        }
    }
}