package com.cla.navigation.utils

import com.cla.navigation.SortModel
import com.konsung.basic.bean.HomeData
import com.konsung.basic.bean.navigation.NavigationBean
import java.util.*

class NavigationUtils {

    companion object {

        /**
         * 为RecyclerView填充数据
         *
         * @param date
         * @return
         */
        fun filledData(data: List<NavigationBean>): MutableList<SortModel> {
            val mSortList = ArrayList<SortModel>()
            val set = mutableSetOf<String>()

            for (i in data.indices) {
                val name = data[i].name
                val sortModel = SortModel()
                sortModel.name = name
                //汉字转换成拼音
                val pinyin = PinyinUtils.getPingYin(name)
                val sortString = pinyin.substring(0, 1).toUpperCase()

                // 正则表达式，判断首字母是否是英文字母
                if (sortString.matches("[A-Z]".toRegex())) {
                    sortModel.letters = sortString.toUpperCase()
                } else {
                    sortModel.letters = "#"
                }

                sortModel.isShowLabel = !set.contains(sortModel.letters)
                set.add(sortModel.letters)

                val list = mutableListOf<HomeData.DatasBean>()

                val articles = data[i].articles
                if (articles != null) {
                    list.addAll(articles.filterNotNull())
                }

                sortModel.articles = list
                mSortList.add(sortModel)
            }
            return mSortList

        }

    }

}