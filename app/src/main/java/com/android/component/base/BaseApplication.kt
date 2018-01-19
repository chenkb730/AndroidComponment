package com.android.component.base

import android.app.Application
import android.arch.lifecycle.ProcessLifecycleOwner
import android.content.Context
import com.android.component.lifecircle.AppLifecycleObserver
import com.hazz.kotlinmvp.utils.DisplayManager
import javax.inject.Inject
import kotlin.properties.Delegates


/**
 * Created by Seven on 2018/1/4.
 *
 */

class BaseApplication : Application() {

    @Inject
    lateinit var appLifecycleObserver: AppLifecycleObserver

    companion object {

        private val TAG = "BaseApplication"

        var context: Context by Delegates.notNull()
            private set
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        DisplayManager.init(this)

//        DaggerAppComponent.builder().application(this).build().inject(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(appLifecycleObserver)
    }
}
