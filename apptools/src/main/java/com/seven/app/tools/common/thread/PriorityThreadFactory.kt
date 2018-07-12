package com.seven.app.tools.common.thread

import android.os.Process
import java.util.concurrent.ThreadFactory

/**
 * Created by Seven on 2018/3/1.
 */
class PriorityThreadFactory constructor(val priority: Int) : ThreadFactory {


    override fun newThread(runnable: Runnable?): Thread {
        return Thread(Runnable {
            try {
                Process.setThreadPriority(priority)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                runnable!!.run()
            }
        })
    }
}