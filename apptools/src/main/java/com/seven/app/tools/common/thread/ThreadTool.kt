package com.seven.app.tools.common.thread

import android.os.Process
import java.util.concurrent.*

/**
 * Created by Seven on 2018/3/1.
 */
class ThreadTool private constructor() {


    private var mBackgroundTask: ThreadPoolExecutor? = null
    private var mMainTask: Executor? = null

    init {
        mBackgroundTask = ThreadPoolExecutor(NUMBER_OF_CORE, NUMBER_OF_CORE * 2, KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT,
                LinkedBlockingQueue<Runnable>(), PriorityThreadFactory(Process.THREAD_PRIORITY_BACKGROUND), DefaultRejectedExecutionHandler())

        mMainTask = MainThreadExecutor()
    }

    companion object {

        val INSTANCE by lazy { ThreadTool() }

        val NUMBER_OF_CORE = Runtime.getRuntime().availableProcessors()

        const val KEEP_ALIVE_TIME = 1L

        val KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS
    }

    fun createExecutorService(): ExecutorService {
        return ThreadPoolExecutor(NUMBER_OF_CORE, NUMBER_OF_CORE * 2, KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT,
                LinkedBlockingQueue<Runnable>())

    }

    fun getBackgroundTasks(): ThreadPoolExecutor? {
        return mBackgroundTask
    }

    fun getMainTask(): Executor? {
        return mMainTask
    }

}