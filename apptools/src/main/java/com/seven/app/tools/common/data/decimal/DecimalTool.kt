package com.seven.app.tools.common.data.decimal

import java.lang.IllegalArgumentException
import java.lang.StringBuilder
import java.lang.UnsupportedOperationException
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*


/**
 * 数值格式化工具
 * Created by Seven on 2018/1/22.
 */
class DecimalTool private constructor() {

    init {
        throw UnsupportedOperationException("please do not init ToolDecimal...")
    }

    companion object {

        private val HUNDRED = BigInteger.valueOf(100)

        /**
         * 自己实现的开平方
         *
         * @param number
         * 数值
         * @param scale
         * 精度
         * @param roundingMode
         * 舍入方法
         *
         * @return 结果
         */
        internal fun sqrt(number: BigDecimal, scale: Int, roundingMode: RoundingMode): BigDecimal {
            if (number < BigDecimal.ZERO) {
                throw ArithmeticException("sqrt with negative")
            }
            val integer = number.toBigInteger()
            val sb = StringBuilder()
            var strInt = integer.toString()
            var lenInt = strInt.length
            if (lenInt % 2 != 0) {
                strInt += '0'
                lenInt++
            }

            val ss = ""

            var res = BigInteger.ZERO
            var rem = BigInteger.ZERO
            for (i in 0..lenInt / 2) {
                res = res.multiply(BigInteger.TEN)
                rem = rem.multiply(HUNDRED)

                val temp = BigInteger(strInt.slice(i * 2..i * 2 + 2))
                rem = rem.add(temp)

                var j = BigInteger.TEN
                while (j > BigInteger.ZERO) {
                    j = j.subtract(BigInteger.ONE)
                    if (res.add(j).multiply(j) <= rem) {
                        break
                    }
                }

                res = res.add(j)
                rem = rem.subtract(res.multiply(j))
                res = res.add(j)
                sb.append(j)
            }
            sb.append('.')
            var fraction = number.subtract(number.setScale(0, RoundingMode.DOWN))
            val fractionLength = (fraction.scale() + 1) / 2
            fraction = fraction.movePointRight(fractionLength * 2)
            val fractionStr = fraction.toPlainString()
            for (i in 0..scale) {
                res = res.multiply(BigInteger.TEN)
                rem = rem.multiply(HUNDRED)

                if (i < fractionLength) {
                    val temp = BigInteger(fractionStr.slice(i * 2..i * 2 + 2))
                    rem = rem.add(temp)
                }

                var j = BigInteger.TEN
                while (j > BigInteger.ZERO) {
                    j = j.subtract(BigInteger.ONE)
                    if (res.add(j).multiply(j) <= rem) {
                        break
                    }
                }
                res = res.add(j)
                rem = rem.subtract(res.multiply(j))
                res = res.add(j)
                sb.append(j)
            }
            return BigDecimal(sb.toString()).setScale(scale, roundingMode)
        }

        /**
         * 返回零位的最高位(“最左侧”)之前的数中指定的int值的二进制补码表示一比特
         *
         *
         * 此方法返回零位的最高位(“最左侧”)前在指定的int值的二进制补码表示法，或32个1位的数量，如果该值为零。
         *
         * @param i
         * 数字
         *
         * @return 结果
         */
        fun numberOfLeadingZeros(i: Long): Int {
            if (i == 0L) {
                return 64
            }
            var n = 1
            var x = i.ushr(32).toInt()
            if (x == 0) {
                n += 32
                x = i.toInt()
            }
            if (x.ushr(16) == 0) {
                n += 16
                x = x shl 16
            }
            if (x.ushr(24) == 0) {
                n += 8
                x = x shl 8
            }
            if (x.ushr(28) == 0) {
                n += 4
                x = x shl 4
            }
            if (x.ushr(30) == 0) {
                n += 2
                x = x shl 2
            }
            n -= x.ushr(31)
            return n
        }

        /**
         * 设置给DecimalFormat用的精度字符创
         *
         * @param scale
         * 精度
         *
         * @return 结果
         */
        fun scale2FormatStr(scale: Int): String {
            val mStrformat = StringBuilder("##0")
            if (scale < 0) {
                throw IllegalArgumentException(
                        "The scale must be a positive integer or zero")
            }
            if (scale > 0) {
                mStrformat.append(".")
                for (i in 0..scale) {
                    mStrformat.append("0")
                }
            }

            return mStrformat.toString()
        }

        private val DEFAULT_RM = RoundingMode.HALF_EVEN
        private val DECIMAL_FORMAT_MAP = HashMap<String, DecimalFormat>()

        fun scale2Format(scale: Int, roundingMode: RoundingMode = DEFAULT_RM): DecimalFormat {
            val cacheKey = scale.toString() + roundingMode.name
            var result: DecimalFormat? = DECIMAL_FORMAT_MAP[cacheKey]
            if (result == null) {
                result = DecimalFormat(scale2FormatStr(scale))
                result.roundingMode = roundingMode
                DECIMAL_FORMAT_MAP.put(cacheKey, result)
            }
            return result
        }

        /**
         * @param value
         * @param scale
         *
         * @return
         */
        fun number2Str(value: Double?, scale: Int): String {
            return BigDecimal(value!!).setScale(scale, RoundingMode.HALF_UP).toString()
        }

        /**
         * @param value
         * @param scale
         *
         * @return
         */
        fun number2double(value: Double?, scale: Int): Double? {
            return BigDecimal(value!!).setScale(scale, RoundingMode.HALF_UP).toDouble()
        }
    }
}