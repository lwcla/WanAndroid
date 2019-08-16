package com.konsung.cla.demo2.util

import android.content.Context
import android.content.Intent
import com.konsung.cla.demo2.aty.FriMainAty

class FriProductUtils : ProductUtils() {

    override fun startMainAty(context: Context) {
        val intent = Intent();
        intent.setClass(context, FriMainAty::class.java)
        context.startActivity(intent)
    }

}