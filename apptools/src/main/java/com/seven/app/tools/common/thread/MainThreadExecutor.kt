package com.seven.app.tools.common.thread

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor

/**
 * Created by Seven on 2018/3/1.
 */
class MainThreadExecutor : Executor {

    private val mHandler = Handler(Looper.getMainLooper())
    override fun execute(runnable: Runnable?) {
        mHandler.post(runnable)



        ThreadTool.INSTANCE.createExecutorService()
    }
}