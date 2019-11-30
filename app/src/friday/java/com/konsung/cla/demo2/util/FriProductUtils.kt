package com.konsung.cla.demo2.util

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import com.konsung.cla.demo2.start.FriMainAty

class FriProductUtils : ProductUtils() {

    override fun startMainAty(activity: Activity) {
        val intent = Intent();
        intent.setClass(activity, FriMainAty::class.java)
//        activity.startActivity(intent)
        activity.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle())
    }

}