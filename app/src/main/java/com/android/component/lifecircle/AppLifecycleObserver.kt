package com.android.component.lifecircle

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import javax.inject.Inject

/**
 *
 * Created by Seven on 2018/1/5.
 */
class AppLifecycleObserver @Inject constructor(context: Context) : LifecycleObserver {


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onEnterForeground() {
        println("app now is  in foreground")
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onEnterBackground() {
        println("app in background")

    }
}