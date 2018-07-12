package com.seven.app.tools.common.thread

import java.util.concurrent.RejectedExecutionHandler
import java.util.concurrent.ThreadPoolExecutor

/**
 * Created by Seven on 2018/3/1.
 */
class DefaultRejectedExecutionHandler : RejectedExecutionHandler {


    override fun rejectedExecution(runnable: Runnable?, executor: ThreadPoolExecutor?) {

    }
}