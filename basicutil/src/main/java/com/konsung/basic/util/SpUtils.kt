package com.konsung.basic.util

import android.content.Context
import android.content.SharedPreferences


class SpUtils {

    companion object {
        private val TAG = SpUtils::class.java.simpleName
        private const val XML_NAME = "cla"

        private fun getSharePreferences(context: Context?): SharedPreferences? {
            return context?.getSharedPreferences(XML_NAME, Context.MODE_PRIVATE)
        }

        fun delete(context: Context, key: String) {
            val editor = getSharePreferences(context)?.edit()
            editor?.remove(key)
            editor?.apply()
            Debug.info(TAG, "SpUtils delete key=$key ")
        }

        fun putString(context: Context, key: String, value: String?) {
            val editor = getSharePreferences(context)?.edit()
            editor?.putString(key, value)
            editor?.apply()
            Debug.info(TAG, "SpUtils putString key=$key value=$value ")
        }

        fun getString(context: Context, key: String, defValue: String): String? {
            val sharedPreferences = getSharePreferences(context)
            val value = sharedPreferences?.getString(key, defValue) ?: defValue
            Debug.info(TAG, "SpUtils getString key=$key value=$value defValue=$defValue")
            return value
        }

        fun putInt(context: Context, key: String, value: Int) {
            val editor = getSharePreferences(context)?.edit()
            editor?.putInt(key, value)
            editor?.apply()
            Debug.info(TAG, "SpUtils putInt key=$key value=$value ")
        }

        fun getInt(context: Context, key: String, defaultValue: Int): Int {
            val sharedPreferences = getSharePreferences(context)
            val value = sharedPreferences?.getInt(key, defaultValue) ?: defaultValue
            Debug.info(TAG, "SpUtils getInt key=$key value=$value defValue=$defaultValue")
            return value
        }

        fun putBoolean(context: Context, key: String, value: Boolean) {
            val editor = getSharePreferences(context)?.edit()
            editor?.putBoolean(key, value)
            editor?.apply()
            Debug.info(TAG, "SpUtils putBoolean key=$key value=$value ")
        }

        fun getBoolean(context: Context, key: String, defValue: Boolean): Boolean {
            val sharedPreferences = getSharePreferences(context)
            val value = sharedPreferences?.getBoolean(key, defValue) ?: defValue
            Debug.info(TAG, "SpUtils getBoolean key=$key value=$value defValue=$defValue")
            return value
        }
    }
}