package org.seven.khttp.log

import okhttp3.ResponseBody

/**
 * http日志打印
 * Created by Seven on 2018/7/16.
 */
interface KHttpLogInterface {

    enum class LOGTAG {
        INFO,
        DEBUG,
        ERROR,
        WARNING,
        VERBOSE;

    }

    fun log(response: ResponseBody?)
}