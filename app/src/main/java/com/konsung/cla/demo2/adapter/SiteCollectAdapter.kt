package com.konsung.cla.demo2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.konsung.basic.bean.site.SiteCollectBean
import com.konsung.basic.util.StringUtils
import com.konsung.cla.demo2.R

class SiteCollectAdapter(private var dataList: List<SiteCollectBean>) : RecyclerView.Adapter<SiteCollectAdapter.SiteCollectViewHolder>() {

    var editListener: EditListener? = null

    fun addData(t: SiteCollectBean) {
        val list = mutableListOf<SiteCollectBean>()
        list.addAll(dataList)
        list.add(0, t)
        dataList = list
        notifyItemInserted(0)
    }

    fun update(list: List<SiteCollectBean>) {
        dataList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SiteCollectViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.adapter_site_collect, parent, false)
        return SiteCollectViewHolder(context, view, editListener)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: SiteCollectViewHolder, position: Int) {
        holder.bind(dataList[position], position)
    }

    class SiteCollectViewHolder(private val context: Context, itemView: View, editListener: EditListener?) : RecyclerView.ViewHolder(itemView) {

        companion object {
            val TAG: String = SiteCollectViewHolder::class.java.simpleName
        }

        private val tvEdit by lazy { itemView.findViewById<TextView>(R.id.tvEdit) }
        private val tvName by lazy { itemView.findViewById<TextView>(R.id.tvName) }
        private val tvLink by lazy { itemView.findViewById<TextView>(R.id.tvLink) }
        private val imvStart by lazy { itemView.findViewById<ImageView>(R.id.imvStart) }

        private var bean: SiteCollectBean? = null
        private var index = -1

        init {
            StringUtils.instance.loadTextIcon(context, tvEdit)

            tvEdit.setOnClickListener {
                bean?.apply {

                    if (id == null) {
                        return@setOnClickListener
                    }

                    editListener?.edit(id!!, index)
                }
            }

            imvStart.setOnClickListener {

            }
        }


        fun bind(bean: SiteCollectBean, position: Int) {
            this.bean = bean
            this.index = position

            tvName.text = StringUtils.instance.formHtml(bean.name)
            tvLink.text = bean.link
        }
    }
}

interface EditListener {

    fun edit(id: Int, position: Int)
}