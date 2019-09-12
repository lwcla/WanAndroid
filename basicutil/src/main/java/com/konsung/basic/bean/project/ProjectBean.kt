package com.konsung.basic.bean.project

import com.konsung.basic.bean.HomeData

data class ProjectBean(val curPage: Int, val datas: List<HomeData.DatasBean?>?, val offset: Int, val over: Boolean, val pageCount: Int, val size: Int, val total: Int)