package com.konsung.cla.demo2.main

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout


class RelativeLayoutBehavior(context: Context, attrs: AttributeSet) : CoordinatorLayout.Behavior<RelativeLayout>(context, attrs) {

    override fun layoutDependsOn(parent: CoordinatorLayout, child: RelativeLayout, dependency: View): Boolean {
        return dependency is Toolbar
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: RelativeLayout, dependency: View): Boolean {
        //计算列表y坐标，最小为0
//        println("height=${dependency.height} translationY=${dependency.translationY}")
        var y = dependency.height + dependency.translationY
        if (y < 0) {
            y = 0f
        }
        child.y = y
        return true
    }
}
