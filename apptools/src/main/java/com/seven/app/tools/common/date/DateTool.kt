package com.seven.app.tools.common.date

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Seven on 2018/3/2.
 */

class DateTool private constructor() {


    private var sdf: SimpleDateFormat? = null

    init {

        sdf = SimpleDateFormat()

    }

    companion object {

        val INSTANCE = { DateTool() }

        val FORMAT_YEAR_STR: String = "yyyy"
        val FORMAT_MONTH_STR = "MM"
        val FORMAT_DAY_STR = "dd"
        val FORMAT_HOUR_STR = "HH"
        val FORMAT_MIN_STR = "mm"
        val FORMAT_SECOND_STR = "ss"


        val SENCOND = 60
        val MINUTE = 1 * SENCOND// 分钟
        val HOUR = 60 * MINUTE// 小时
        val DAY = 24 * HOUR// 天
        val MONTH = 30 * DAY// 月
        val YEAR = 12 * MONTH// 年


    }


    /**
     * 创建日期格式
     */
    fun createFormatPattern(year: String = "-", month: String = "-", day: String = "", hour: String = ":", min: String = ":", second: String = ""): String {
        return "$FORMAT_YEAR_STR$year$FORMAT_MONTH_STR$month$FORMAT_DAY_STR$' '$FORMAT_HOUR_STR$hour$FORMAT_MIN_STR$min$FORMAT_SECOND_STR$second"
    }

    /**
     * 获取格式化之后的时间
     */
    fun getTime(date: Date = Date(), pattern: String = createFormatPattern()): String {
        sdf!!.applyPattern(pattern)
        return sdf!!.format(date)
    }

    /**
     * 时间字符串转时间
     */
    fun formatStrToDate(dateTime: String, format: String = createFormatPattern()): Date? {
        sdf!!.applyPattern(format)
        return sdf!!.parse(dateTime)
    }

    /**
     * 时间戳转时间
     */
    fun formatLongToDate(dateTime: Long, format: String = createFormatPattern()): Date? {
        return formatStrToDate(getTime(Date(dateTime), format), format)
    }
}
