package com.seven.app.tools.common.data.text

import android.os.Build
import android.support.annotation.RequiresApi
import com.seven.app.tools.common.constant.UnitConst
import java.io.*
import java.util.*


/**
 * 格式化工具类
 * Created by Seven on 2018/1/23.
 */
class ConvertTool private constructor() {

    init {
        throw UnsupportedOperationException("please do not init ConvertTool")
    }

    companion object {
        private val HEX_DIGITS = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')

        /**
         * byteArr转hexString
         *
         *例如： bytes2HexString(new byte[] { 0, (byte) 0xa8 }) returns 00A8
         *
         * @param bytes
         * 字节数组
         *
         * @return 16进制大写字符串
         */
        fun bytes2HexString(bytes: ByteArray?): String? {
            if (bytes == null) {
                return null
            }
            val len = bytes.size
            if (len <= 0) {
                return null
            }
            val ret = CharArray(len shl 1)
            var i = 0
            var j = 0
            while (i < len) {
                ret[j++] = HEX_DIGITS[bytes[i].toInt().ushr(4) and 0x0f]
                ret[j++] = HEX_DIGITS[bytes[i].toInt() and 0x0f]
                i++
            }
            return String(ret)
        }

        /**
         * hexString转byteArr
         *
         *例如： hexString2Bytes("00A8") returns { 0, (byte) 0xA8 }
         *
         * @param hexString
         * 十六进制字符串
         *
         * @return 字节数组
         */
        fun hexString2Bytes(hexString: String): ByteArray? {
            var hexString = hexString
            if (StringTool.isSpace(hexString)) {
                return null
            }
            var len = hexString.length
            if (len % 2 != 0) {
                hexString = "0" + hexString
                len += 1
            }
            val hexBytes = hexString.toUpperCase().toCharArray()
            val ret = ByteArray(len shr 1)
            var i = 0
            while (i < len) {
                ret[i shr 1] = (hex2Dec(hexBytes[i]) shl 4 or hex2Dec(hexBytes[i + 1])).toByte()
                i += 2
            }
            return ret
        }

        /**
         * hexChar转int
         *
         * @param hexChar
         * hex单个字节
         *
         * @return 0..15
         */
        private fun hex2Dec(hexChar: Char): Int {
            return if (hexChar in '0'..'9') {
                hexChar - '0'
            } else if (hexChar in 'A'..'F') {
                hexChar - 'A' + 10
            } else {
                throw IllegalArgumentException()
            }
        }

        /**
         * charArr转byteArr
         *
         * @param chars
         * 字符数组
         *
         * @return 字节数组
         */
        fun chars2Bytes(chars: CharArray?): ByteArray? {
            if (chars == null || chars.isEmpty()) {
                return null
            }
            val len = chars.size
            val bytes = ByteArray(len)
            for (i in 0 until len) {
                bytes[i] = chars[i].toByte()
            }
            return bytes
        }

        /**
         * byteArr转charArr
         *
         * @param bytes
         * 字节数组
         *
         * @return 字符数组
         */
        fun bytes2Chars(bytes: ByteArray?): CharArray? {
            if (bytes == null) {
                return null
            }
            val len = bytes.size
            if (len <= 0) {
                return null
            }
            val chars = CharArray(len)
            for (i in 0 until len) {
                chars[i] = (bytes[i].toInt() and 0xff).toChar()
            }
            return chars
        }

        /**
         * 以unit为单位的内存大小转字节数
         *
         * @param memorySize
         * 大小
         * @param unit
         * 单位类型   * [UnitConst.MemoryUnit.BYTE]: 字节  * [UnitConst.MemoryUnit.KB]  : 千字节
         *  * [UnitConst.MemoryUnit.MB]  : 兆  * [UnitConst.MemoryUnit.GB]  : GB
         *
         * @return 字节数
         */
        fun memorySize2Byte(memorySize: Long, unit: UnitConst.MemoryUnit): Long {
            if (memorySize < 0) {
                return -1
            }
            return when (unit) {
                UnitConst.MemoryUnit.BYTE -> memorySize
                UnitConst.MemoryUnit.KB -> memorySize * UnitConst.KB
                UnitConst.MemoryUnit.MB -> memorySize * UnitConst.MB
                UnitConst.MemoryUnit.GB -> memorySize * UnitConst.GB
            }
        }

        /**
         * 字节数转以unit为单位的内存大小
         *
         * @param byteNum
         * 字节数
         * @param unit
         * 单位类型   * [UnitConst.MemoryUnit.BYTE]: 字节  * [UnitConst.MemoryUnit.KB]  : 千字节
         *  * [UnitConst.MemoryUnit.MB]  : 兆  * [UnitConst.MemoryUnit.GB]  : GB
         *
         * @return 以unit为单位的size
         */
        fun byte2MemorySize(byteNum: Long, unit: UnitConst.MemoryUnit): Double {
            if (byteNum < 0) {
                return -1.0
            }
            return when (unit) {
                UnitConst.MemoryUnit.BYTE -> byteNum.toDouble()
                UnitConst.MemoryUnit.KB -> byteNum.toDouble() / UnitConst.KB
                UnitConst.MemoryUnit.MB -> byteNum.toDouble() / UnitConst.MB
                UnitConst.MemoryUnit.GB -> byteNum.toDouble() / UnitConst.GB
            }
        }

        /**
         * 字节数转合适内存大小
         *
         *保留3位小数
         *
         * @param byteNum
         * 字节数
         *
         * @return 合适内存大小
         */
        fun byte2FitMemorySize(byteNum: Long): String {
            return when {
                byteNum < 0 -> "shouldn't be less than zero!"
                byteNum < UnitConst.KB -> String.format("%.3fB", byteNum + 0.0005)
                byteNum < UnitConst.MB -> String.format("%.3fKB", byteNum / UnitConst.KB + 0.0005)
                byteNum < UnitConst.GB -> String.format("%.3fMB", byteNum / UnitConst.MB + 0.0005)
                else -> String.format("%.3fGB", byteNum / UnitConst.GB + 0.0005)
            }
        }

        /**
         * 以unit为单位的时间长度转毫秒时间戳
         *
         * @param timeSpan
         * 毫秒时间戳
         * @param unit
         * 单位类型   * [UnitConst.TimeUnit.MSEC]: 毫秒  * [UnitConst.TimeUnit.SEC]: 秒
         *  * [UnitConst.TimeUnit.MIN]: 分  * [UnitConst.TimeUnit.HOUR]: 小时  * [         ][UnitConst.TimeUnit.DAY]: 天
         *
         * @return 毫秒时间戳
         */
        fun timeSpan2Millis(timeSpan: Long, unit: UnitConst.TimeUnit): Long {
            return when (unit) {
                UnitConst.TimeUnit.MSEC -> timeSpan
                UnitConst.TimeUnit.SEC -> timeSpan * UnitConst.SEC
                UnitConst.TimeUnit.MIN -> timeSpan * UnitConst.MIN
                UnitConst.TimeUnit.HOUR -> timeSpan * UnitConst.HOUR
                UnitConst.TimeUnit.DAY -> timeSpan * UnitConst.DAY
            }
        }

        /**
         * 毫秒时间戳转以unit为单位的时间长度
         *
         * @param millis
         * 毫秒时间戳
         * @param unit
         * 单位类型   * [UnitConst.TimeUnit.MSEC]: 毫秒  * [UnitConst.TimeUnit.SEC]: 秒
         *  * [UnitConst.TimeUnit.MIN]: 分  * [UnitConst.TimeUnit.HOUR]: 小时  * [         ][UnitConst.TimeUnit.DAY]: 天
         *
         * @return 以unit为单位的时间长度
         */
        fun millis2TimeSpan(millis: Long, unit: UnitConst.TimeUnit): Long {
            return when (unit) {
                UnitConst.TimeUnit.MSEC -> millis
                UnitConst.TimeUnit.SEC -> millis / UnitConst.SEC
                UnitConst.TimeUnit.MIN -> millis / UnitConst.MIN
                UnitConst.TimeUnit.HOUR -> millis / UnitConst.HOUR
                UnitConst.TimeUnit.DAY -> millis / UnitConst.DAY
            }
        }

        /**
         * 毫秒时间戳转合适时间长度
         *
         * @param millis
         * 毫秒时间戳
         *
         *小于等于0，返回null
         * @param precision
         * 精度   * precision = 0，返回null  * precision = 1，返回天  * precision = 2，返回天和小时
         *  * precision = 3，返回天、小时和分钟  * precision = 4，返回天、小时、分钟和秒  * precision &gt;=
         * 5，返回天、小时、分钟、秒和毫秒
         *
         * @return 合适时间长度
         */
        fun millis2FitTimeSpan(millis: Long, precision: Int): String? {
            var millis = millis
            var precision = precision
            if (millis <= 0 || precision <= 0) {
                return null
            }
            val sb = StringBuilder()
            val units = arrayOf("天", "小时", "分钟", "秒", "毫秒")
            val unitLen = intArrayOf(86400000, 3600000, 60000, 1000, 1)
            precision = Math.min(precision, 5)
            for (i in 0 until precision) {
                if (millis >= unitLen[i]) {
                    val mode = millis / unitLen[i]
                    millis -= mode * unitLen[i]
                    sb.append(mode).append(units[i])
                }
            }
            return sb.toString()
        }

        /**
         * bytes转bits
         *
         * @param bytes
         * 字节数组
         *
         * @return bits
         */
        fun bytes2Bits(bytes: ByteArray): String {
            val sb = StringBuilder()
            for (aByte in bytes) {
                for (j in 7 downTo 0) {
                    sb.append(if (aByte.toInt().shr(j) and 0x01 == 0) '0' else '1')
                }
            }
            return sb.toString()
        }

        /**
         * bits转bytes
         *
         * @param bits
         * 二进制
         *
         * @return bytes
         */
        fun bits2Bytes(bits: String): ByteArray {
            var bits = bits
            val lenMod = bits.length % 8
            var byteLen = bits.length / 8
            // 不是8的倍数前面补0
            if (lenMod != 0) {
                for (i in lenMod..7) {
                    bits = "0" + bits
                }
                byteLen++
            }
            val bytes = ByteArray(byteLen)
            for (i in 0 until byteLen) {
                for (j in 0..7) {
                    bytes[i] = bytes[i].toInt().shl(1).toByte()
                    bytes[i] = bytes[i].toInt().or(bits[i * 8 + j] - '0').toByte()
                }
            }
            return bytes
        }

        /**
         * inputStream转outputStream
         *
         * @param is
         * 输入流
         *
         * @return outputStream子类
         */
        @Throws(IOException::class)
        fun input2OutputStream(`is`: InputStream?): ByteArrayOutputStream? {
            if (`is` == null) {
                return null
            }
            ByteArrayOutputStream().use({ os ->
                val b = ByteArray(UnitConst.KB)
                var len = 0
                while (`is`!!.read(b, 0, UnitConst.KB).apply { len = this } != -1) {
                    os.write(b, 0, len)
                }
                return os
            })
        }

        /**
         * inputStream转byteArr
         *
         * @param is
         * 输入流
         *
         * @return 字节数组
         */
        @Throws(IOException::class)
        fun inputStream2Bytes(`is`: InputStream?): ByteArray? {
            return if (`is` == null) {
                null
            } else input2OutputStream(`is`)!!.toByteArray()
        }

        /**
         * byteArr转inputStream
         *
         * @param bytes
         * 字节数组
         *
         * @return 输入流
         */
        fun bytes2InputStream(bytes: ByteArray?): InputStream? {
            return if (bytes == null || bytes.isEmpty()) {
                null
            } else ByteArrayInputStream(bytes)
        }

        /**
         * outputStream转byteArr
         *
         * @param out
         * 输出流
         *
         * @return 字节数组
         */
        fun outputStream2Bytes(out: OutputStream?): ByteArray? {
            return if (out == null) {
                null
            } else (out as ByteArrayOutputStream).toByteArray()
        }

        /**
         * outputStream转byteArr
         *
         * @param bytes
         * 字节数组
         *
         * @return 字节数组
         */
        @Throws(IOException::class)
        fun bytes2OutputStream(bytes: ByteArray?): OutputStream? {
            if (bytes == null || bytes.isEmpty()) {
                return null
            }
            ByteArrayOutputStream().use({ os ->
                os.write(bytes)
                return os
            })
        }

        /**
         * inputStream转string按编码
         *
         * @param is
         * 输入流
         * @param charsetName
         * 编码格式
         *
         * @return 字符串
         */
        @Throws(IOException::class)
        fun inputStream2String(`is`: InputStream?, charsetName: String): String? {
            return if (`is` == null || StringTool.isSpace(charsetName)) {
                null
            } else String(inputStream2Bytes(`is`)!!, charset(charsetName))
        }

        /**
         * string转inputStream按编码
         *
         * @param string
         * 字符串
         * @param charsetName
         * 编码格式
         *
         * @return 输入流
         */
        @Throws(UnsupportedEncodingException::class)
        fun string2InputStream(string: String?, charsetName: String): InputStream? {
            return if (string == null || StringTool.isSpace(charsetName)) {
                null
            } else ByteArrayInputStream(string.toByteArray(charset(charsetName)))

        }

        /**
         * outputStream转string按编码
         *
         * @param out
         * 输出流
         * @param charsetName
         * 编码格式
         *
         * @return 字符串
         */
        @Throws(UnsupportedEncodingException::class)
        fun outputStream2String(out: OutputStream?, charsetName: String): String? {
            return if (out == null || StringTool.isSpace(charsetName)) {
                null
            } else String(outputStream2Bytes(out)!!, charset(charsetName))

        }

        /**
         * string转outputStream按编码
         *
         * @param string
         * 字符串
         * @param charsetName
         * 编码格式
         *
         * @return 输入流
         */
        @Throws(IOException::class)
        fun string2OutputStream(string: String?, charsetName: String): OutputStream? {
            return if (string == null || StringTool.isSpace(charsetName)) {
                null
            } else bytes2OutputStream(string.toByteArray(charset(charsetName)))
        }

        /**
         * outputStream转inputStream
         *
         * @param out
         * 输出流
         *
         * @return inputStream子类
         */
        fun output2InputStream(out: OutputStream?): ByteArrayInputStream? {
            return if (out == null) {
                null
            } else ByteArrayInputStream((out as ByteArrayOutputStream).toByteArray())
        }

        fun str2map(str: String, tokenStr: String, splitStr: String): Map<String, String> {
            val tokenSTK = StringTokenizer(str, tokenStr)
            val result = mutableMapOf<String, String>()
            while (tokenSTK.hasMoreTokens()) {
                val nextStr = tokenSTK.nextToken()
                val splitSTK = StringTokenizer(nextStr, splitStr)
                if (splitSTK.countTokens() === 2) {
                    result.put(splitSTK.nextToken(), splitSTK.nextToken())
                }
            }
            return result
        }

        @RequiresApi(Build.VERSION_CODES.N)
        fun map2str(params: Map<String, Any>, middleStr: String, endStr: String): String {
            val result = StringBuilder()
            params.forEach { key, value -> result.append(key).append(middleStr).append(value.toString()).append(endStr) }
            return result.toString()
        }
    }
}