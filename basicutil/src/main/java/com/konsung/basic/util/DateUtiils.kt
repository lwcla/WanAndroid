package com.konsung.basic.util

import java.text.SimpleDateFormat
import java.util.*

class DateUtiils {

    companion object {

        fun convertTime(time: Long) :String{
            val date = Date(time)
            val format = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())
            return format.format(date)
        }
    }

}