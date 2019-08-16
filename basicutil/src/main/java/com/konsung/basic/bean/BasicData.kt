package com.konsung.basic.bean

data class BasicData<T>(val errorCode: Int = 0, val errorMsg: String?, val data: T?)

