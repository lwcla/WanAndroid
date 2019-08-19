package com.konsung.basic.util

import java.text.SimpleDateFormat
import java.util.*


class DateUtils {

    companion object {

        private const val ONE_MINUTE: Long = 60
        private const val ONE_HOUR: Long = 3600
        private const val ONE_DAY: Long = 86400
        private const val ONE_MONTH: Long = 2592000
        private const val ONE_YEAR: Long = 31104000

        fun convertTime(time: Long): String {
            val date = Date(time)
            val format = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())
            return format.format(date)
        }

        /**
         * 距离今天多久
         * @param time  time
         * @return
         */
        fun fromToday(time: Long): String {
            return fromToday(Date(time))
        }

        /**
         * 距离今天多久
         * @param date  date
         * @return
         */
        fun fromToday(date: Date): String {
            val calendar = Calendar.getInstance()
            calendar.time = date

            val time = date.time / 1000
            val now = Date().time / 1000
            val ago = now - time

            if (ago <= ONE_HOUR)
                return "${ago / ONE_MINUTE}分钟前"
            else if (ago <= ONE_DAY)
                return "${ago / ONE_HOUR}小时${ago % ONE_HOUR / ONE_MINUTE}分钟前"
            else if (ago <= ONE_DAY * 2)
                return "昨天${calendar.get(Calendar.HOUR_OF_DAY)}点${calendar.get(Calendar.MINUTE)}分"
            else if (ago <= ONE_DAY * 3)
                return "前天${calendar.get(Calendar.HOUR_OF_DAY)}点${calendar.get(Calendar.MINUTE)}分"
            else if (ago <= ONE_MONTH) {
                val day = ago / ONE_DAY
                return "${day}天前${calendar.get(Calendar.HOUR_OF_DAY)}点${calendar.get(Calendar.MINUTE)}分"
            } else if (ago <= ONE_YEAR) {
                val month = ago / ONE_MONTH
                val day = ago % ONE_MONTH / ONE_DAY
                return "${month}个月${day}天前${calendar.get(Calendar.HOUR_OF_DAY)}点${calendar.get(Calendar.MINUTE)}分"
            } else {
                val year = ago / ONE_YEAR
                // JANUARY which is 0 so month+1
                val month = calendar.get(Calendar.MONTH) + 1;
                return "${year}年前${month}月${calendar.get(Calendar.DATE)}日"
            }

        }
    }

}