package com.seven.app.tools.common.data.text

import android.os.Build
import android.support.annotation.RequiresApi
import java.util.*

/**
 * 字符串工具相关
 * Created by Seven on 2018/1/23.
 */
class StringTool private constructor() {

    init {
        throw UnsupportedOperationException("please do not init StringTool")
    }

    companion object {


        /**
         * 判断字符串是否为null或长度为0
         */
        fun isEmpty(s: CharSequence?): Boolean {
            return s == null || !s.isEmpty()
        }

        /**
         * 判断字符串是否为null或全为空格
         */
        fun isSpace(s: String?): Boolean {
            return s == null || s.trim { it <= ' ' }.isEmpty()
        }


        /**
         * 字符串为 null 或者内部字符全部为 ' ' '\t' '\n' '\r' 这四类字符时返回 true
         */
        fun isBlank(str: String?): Boolean {
            if (str == null) {
                return true
            }
            val len = str.length
            if (len == 0) {
                return true
            }
            for (i in 0 until len) {
                when (str[i]) {
                    ' ', '\t', '\n', '\r' -> {
                    }
                    else -> return false
                }
            }
            return true
        }

        /**
         * 判断字符串不为空返回true
         *
         * @param str
         *
         * @return
         */
        fun notBlank(str: String): Boolean {
            return !isBlank(str)
        }

        /**
         * 判断多个字符串不为空字符串返回true
         *
         * @param strings
         *
         * @return
         */
        fun notBlank(vararg strings: String): Boolean {
            if (strings == null) {
                return false
            }
            return strings.none { isBlank(it) }
        }

        /**
         * 判断多个Object对象不为null，则返回true
         *
         * @param paras
         *
         * @return
         */
        fun notNull(vararg paras: Any?): Boolean {
            if (paras == null) {
                return false
            }
            return !paras.contains(null)
        }

        /**
         * 判断两字符串是否相等
         *
         * @param a
         * 待校验字符串a
         * @param b
         * 待校验字符串b
         *
         * @return `true`: 相等<br></br>`false`: 不相等
         */
        fun equals(a: CharSequence?, b: CharSequence?): Boolean {
            if (a == b) {
                return true
            }
            var length = 0
            return if (a != null && b != null && a.length.apply { length = this } == b.length) {
                return if (a is String && b is String) {
                    a == b
                } else {
                    (0 until length).none { a[it] != b[it] }
                }
            } else false
        }


        /**
         * 判断两字符串忽略大小写是否相等
         */
        @RequiresApi(Build.VERSION_CODES.KITKAT)
        fun equalsIgnoreCase(a: String, b: String?): Boolean {
            return Objects.equals(a, b) || b != null && a.length == b.length && a.regionMatches(0, b, 0, b.length, ignoreCase = true)
        }

        /**
         * null转为长度为0的字符串
         *
         * @param s
         * 待转字符串
         *
         * @return s为null转为长度为0字符串，否则不改变
         */
        fun null2Length0(s: String?): String {
            return s ?: ""
        }

        /**
         * 返回字符串长度
         *
         * @param s
         * 字符串
         *
         * @return null返回0，其他返回自身长度
         */
        fun length(s: CharSequence?): Int {
            return s?.length ?: 0
        }

        /**
         * 首字母大写
         *
         * @param s
         * 待转字符串
         *
         * @return 首字母大写字符串
         */
        fun upperFirstLetter(s: String): String {
            if (isBlank(s)) {
                return ""
            }

            val chars = s.toCharArray()
            if (Character.isUpperCase(chars[0])) {
                return s
            }

            chars[0] = Character.toUpperCase(chars[0])
            return String(chars)
        }

        /**
         * 首字母小写
         *
         * @param s
         * 待转字符串
         *
         * @return 首字母小写字符串
         */
        fun lowerFirstLetter(s: String): String {
            if (isBlank(s)) {
                return ""
            }

            val chars = s.toCharArray()
            if (Character.isLowerCase(chars[0])) {
                return s
            }

            chars[0] = Character.toLowerCase(chars[0])
            return String(chars)
        }

        /**
         * 变全大写
         *
         * @param s
         *
         * @return
         */
        fun toUpper(s: String): String {
            return if (isBlank(s)) {
                ""
            } else s.toUpperCase()

        }

        /**
         * 变全小写
         *
         * @param s
         *
         * @return
         */
        fun toLower(s: String): String {
            return if (isBlank(s)) {
                ""
            } else s.toLowerCase()

        }

        /**
         * 反转字符串
         *
         * @param s
         * 待反转字符串
         *
         * @return 反转字符串
         */
        fun reverse(s: String): String {
            val len = length(s)
            if (len == 0) {
                return s
            }
            val mid = len shr 1
            val chars = s.toCharArray()
            var c: Char
            for (i in 0 until mid) {
                c = chars[i]
                chars[i] = chars[len - i - 1]
                chars[len - i - 1] = c
            }
            return String(chars)
        }

        /**
         * 转化为半角字符
         *
         * @param s
         * 待转字符串
         *
         * @return 半角字符串
         */
        fun toDBC(s: String): String? {
            if (isEmpty(s)) {
                return s
            }
            val chars = s.toCharArray()
            var i = 0
            val len = chars.size
            while (i < len) {
                if (chars[i].toInt() == 12288) {
                    chars[i] = ' '
                } else if (65281 <= chars[i].toInt() && chars[i].toInt() <= 65374) {
                    chars[i] = (chars[i].toInt() - 65248).toChar()
                } else {
                    chars[i] = chars[i]
                }
                i++
            }
            return String(chars)
        }

        /**
         * 转化为全角字符
         *
         * @param s
         * 待转字符串
         *
         * @return 全角字符串
         */
        fun toSBC(s: String): String? {
            if (isEmpty(s)) {
                return s
            }
            val chars = s.toCharArray()
            var i = 0
            val len = chars.size
            while (i < len) {
                if (chars[i] == ' ') {
                    chars[i] = 12288.toChar()
                } else if (33 <= chars[i].toInt() && chars[i].toInt() <= 126) {
                    chars[i] = (chars[i].toInt() + 65248).toChar()
                } else {
                    chars[i] = chars[i]
                }
                i++
            }
            return String(chars)
        }

        /**
         * 将带（下划线）_的字符串格式化驼峰命名法
         *
         * @param stringWithUnderline
         *
         * @return
         */
        fun toCamelCase(stringWithUnderline: String): String {
            var stringWithUnderline = stringWithUnderline
            if (stringWithUnderline.indexOf('_') == -1) {
                return stringWithUnderline
            }

            stringWithUnderline = stringWithUnderline.toLowerCase()
            val fromArray = stringWithUnderline.toCharArray()
            val toArray = CharArray(fromArray.size)
            var j = 0
            var i = 0
            while (i < fromArray.size) {
                if (fromArray[i] == '_') {
                    // 当前字符为下划线时，将指针后移一位，将紧随下划线后面一个字符转成大写并存放
                    i++
                    if (i < fromArray.size) {
                        toArray[j++] = Character.toUpperCase(fromArray[i])
                    }
                } else {
                    toArray[j++] = fromArray[i]
                }
                i++
            }
            return String(toArray, 0, j)
        }

        /**
         * 将字符串数据组拼接一个字符串
         *
         * @param stringArray
         *
         * @return
         */
        fun join(stringArray: Array<String>): String {
            val sb = StringBuilder()
            for (i in stringArray.indices) {
                sb.append(stringArray[i])
            }
            return sb.toString()
        }

        /**
         * 将字符串数组拼接一个字符串，拼接时数组每个对象都会间隔拼接separator字符串
         *
         * @param stringArray
         * @param separator
         *
         * @return
         */
        fun join(stringArray: Array<String>, separator: String): String {
            val sb = StringBuilder()
            for (i in stringArray.indices) {
                if (i > 0) {
                    sb.append(separator)
                }
                sb.append(stringArray[i])
            }
            return sb.toString()
        }
    }

}