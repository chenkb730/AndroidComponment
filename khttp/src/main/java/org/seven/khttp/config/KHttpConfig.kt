package org.seven.khttp.config

import org.seven.khttp.log.CustomerHttpLog
import org.seven.khttp.log.KHttpLogInterface

/**
 * 配置工具
 * Created by Seven on 2018/7/16.
 */
class KHttpConfig private constructor() {

    companion object {
        fun getInstance(): KHttpConfig {
            return Holder.INSTANCE
        }
    }

    private object Holder {
        val INSTANCE = KHttpConfig()
    }

    //是否需要显示log
    public var showLog: Boolean?
        set(value) {
            showLog = value
        }
        get() {
            return showLog ?: false
        }

    //需要显示什么样的log
    var logTag: KHttpLogInterface.LOGTAG?
        set(value) {
            logTag = value
        }
        get() {
            return logTag ?: KHttpLogInterface.LOGTAG.DEBUG
        }

    //配置自定义的log
    var kHttpLogInterface: KHttpLogInterface?
        set(value) {
            kHttpLogInterface = value
        }
        get() {
            return kHttpLogInterface ?: CustomerHttpLog()
        }


}