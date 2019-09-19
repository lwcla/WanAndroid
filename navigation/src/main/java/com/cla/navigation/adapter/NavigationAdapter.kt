package com.cla.navigation.adapter

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.cla.navigation.R
import com.cla.navigation.SortModel

class NavigationAdapter(private val dataList: MutableList<SortModel>) : RecyclerView.Adapter<NavigationAdapter.NavigationViewHolder>() {

    companion object {
        val TAG = NavigationAdapter::class.java.simpleName
    }

    private val pool = RecyclerView.RecycledViewPool()
    var clickListener: View.OnClickListener? = null

    /**
     * 根据ListView的当前位置获取分类的首字母的char ascii值
     */
    fun getSectionForPosition(position: Int): Int {
        return dataList[position].letters.first().toInt()
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    fun getPositionForSection(section: Int): Int {
        for (i in 0 until itemCount) {
            val sortStr = dataList[i].letters
            val firstChar = sortStr.toUpperCase()[0]
            if (firstChar.toInt() == section) {
                return i
            }
        }
        return -1
    }

    fun update(t: List<SortModel>) {
        dataList.clear()
        dataList.addAll(t)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavigationViewHolder {

        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.adapter_navigation, parent, false)
        return NavigationViewHolder(context, view, clickListener, pool)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: NavigationViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    class NavigationViewHolder(context: Context, itemView: View, listener: View.OnClickListener?, pool: RecyclerView.RecycledViewPool) : RecyclerView.ViewHolder(itemView) {

        private val tvName by lazy { itemView.findViewById<TextView>(R.id.tvName) }
        private val rvData by lazy { itemView.findViewById<RecyclerView>(R.id.rvData) }
        private val adapter2: NavigationAdapter2

        init {
            rvData.setRecycledViewPool(pool)

            adapter2 = NavigationAdapter2(mutableListOf())
            adapter2.clickListener = listener

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

            rvData.layoutManager = chipsLayoutManager
            rvData.adapter = adapter2
        }

        fun bind(bean: SortModel) {
            tvName.text = bean.name
            adapter2.refreshData(bean.articles ?: listOf())
        }
    }
}
