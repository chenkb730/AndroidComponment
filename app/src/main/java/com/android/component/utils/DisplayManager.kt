package com.hazz.kotlinmvp.utils

import android.content.Context
import android.util.DisplayMetrics

/**
 * Created by Seven on 2018/1/4.
 * desc:
 */

object DisplayManager {
    init {

    }

    private var displayMetrics: DisplayMetrics? = null

    private var screenWidth: Int? = null

    private var screenHeight: Int? = null

    private var screenDpi: Int? = null

    fun init(context: Context) {
        displayMetrics = context.resources.displayMetrics
        screenWidth = displayMetrics?.widthPixels
        screenHeight = displayMetrics?.heightPixels
        screenDpi = displayMetrics?.densityDpi
    }


    fun getScreenWidth(): Int? = screenWidth

    fun getScreenHeight(): Int? = screenHeight

    fun dip2px(dipValue: Float): Int? {
        val scale = displayMetrics?.density
        return (dipValue * scale!! + 0.5f).toInt()
    }
}