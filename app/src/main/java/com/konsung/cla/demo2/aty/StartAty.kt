package com.konsung.cla.demo2.aty

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.konsung.cla.demo2.App

class StartAty : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.productUtils.startMainAty(this)
        finish()
    }
}
