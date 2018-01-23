package com.seven.app.tools.common.constant

/**
 * Created by Seven on 2018/1/23.
 */
class UnitConst private constructor() {

    init {
        throw UnsupportedOperationException("please do not init UnitConst")
    }
    //endregion

    enum class MemoryUnit {
        BYTE,
        KB,
        MB,
        GB
    }

    enum class TimeUnit {
        /**
         * 毫秒
         */
        MSEC,
        /**
         * 秒
         */
        SEC,
        /**
         * 分钟
         */
        MIN,
        /**
         * 小时
         */
        HOUR,
        /**
         * 天
         */
        DAY
    }

    companion object {

        //region 存储相关常量
        /**
         * KB与Byte的倍数
         */
        val KB = 1024
        /**
         * MB与Byte的倍数
         */
        val MB = 1048576
        /**
         * GB与Byte的倍数
         */
        val GB = 1073741824
        /**
         * 秒与毫秒的倍数
         */
        val SEC = 1000
        /**
         * 分与毫秒的倍数
         */
        val MIN = 60000
        //endregion

        //region 时间相关常量
        /**
         * 时与毫秒的倍数
         */
        val HOUR = 3600000
        /**
         * 天与毫秒的倍数
         */
        val DAY = 86400000
    }
}