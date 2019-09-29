package com.konsung.cla.demo2.aty

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.konsung.cla.demo2.App

class StartAty : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.productUtils.startMainAty(this)
//        App.productUtils.startCollectAty(this)
        finish()
    }
}
