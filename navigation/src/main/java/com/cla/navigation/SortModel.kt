package com.cla.navigation

import com.konsung.basic.bean.HomeData

class SortModel {

    var name: String? = null
    var letters: String = ""//显示拼音的首字母

    var isShowLabel: Boolean = false
    var articles: List<HomeData.DatasBean>? = null
}
