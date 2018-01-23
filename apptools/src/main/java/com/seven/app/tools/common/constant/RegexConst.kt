package com.seven.app.tools.common.constant

import java.lang.UnsupportedOperationException

/**
 * Created by Seven on 2018/1/22.
 */
object RegexConst {

    init {
        throw  UnsupportedOperationException("please do not init")
    }


    object {
        /**
         * 正则：金额
         */
        val REGEX_MONEY = "^(([1-9]\\d*)|0)(\\.\\d{1,2})?$"
        /**
         * 正则：英文单词或者数字
         */
        val REGEX_WORD_OR_NUMBER = "^[A-Za-z0-9]+$"
        /**
         * 正则：手机号（简单）
         */
        val REGEX_MOBILE_SIMPLE = "^[1]\\d{10}$"
        /**
         * 正则：手机号（精准）
         * 13 14 15 16 17 18 19 开头都可以
         */
        val REGEX_MOBILE_EXACT = "^1[3|4|5|6|7|8|9][0-9]\\d{8}$"
        // public static final Pattern REGEX_MOBILE_EXACT = compile("^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|(147))\\d{8}$");
        /**
         * 正则：电话号码
         */
        val REGEX_TEL = "^0\\d{2,3}[- ]?\\d{7,8}"
        /**
         * 正则：身份证号码15位
         */
        val REGEX_ID_CARD15 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$"
        /**
         * 正则：身份证号码18位
         */
        val REGEX_ID_CARD18 = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9Xx])$"
        /**
         * 正则：邮箱
         */
        val REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$"
        /**
         * 正则：URL
         */
        val REGEX_URL = "^(http|https)://.*$"
        //public static final Pattern REGEX_URL = compile("[a-zA-z]+://[^\\s]*");
        /**
         * 正则：汉字
         */
        val REGEX_ZH = "^[\\u4e00-\\u9fa5]+$"

        /**
         * 正则：用户名，取值范围为a-z,A-Z,0-9,"_",汉字，不能以"_"结尾,用户名必须是6-20位
         */
        val REGEX_USERNAME = "^[A-Za-z\\u4e00-\\u9fa5]{4,30}(?<!_)$"
        /**
         * 正则：yyyy-MM-dd格式的日期校验，已考虑平闰年
         */
        val REGEX_DATE = "^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$"
        /**
         * 正则：IP地址
         */
        val REGEX_IP = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)"
        /**
         * 正则：双字节字符(包括汉字在内)
         */
        val REGEX_DOUBLE_BYTE_CHAR = "[^\\x00-\\xff]"
        /**
         * 正则：空白行
         */
        val REGEX_BLANK_LINE = "\\n\\s*\\r"
        /**
         * 正则：QQ号
         */
        val REGEX_QQ_NUM = "[1-9][0-9]{4,}"
        /**
         * 正则：中国邮政编码
         */
        val REGEX_ZIP_CODE = "[1-9]\\d{5}(?!\\d)"

        //region 以下摘自http://tool.oschina.net/regex
        /**
         * 正则：正整数
         */
        val REGEX_POSITIVE_INTEGER = "^[1-9]\\d*$"
        /**
         * 正则：负整数
         */
        val REGEX_NEGATIVE_INTEGER = "^-[1-9]\\d*$"
        /**
         * 正则：整数
         */
        val REGEX_INTEGER = "^-?[1-9]\\d*$"
        /**
         * 正则：非负整数(正整数 + 0)
         */
        val REGEX_NOT_NEGATIVE_INTEGER = "^[1-9]\\d*|0$"
        /**
         * 正则：非正整数（负整数 + 0）
         */
        val REGEX_NOT_POSITIVE_INTEGER = "^-[1-9]\\d*|0$"
        /**
         * 正则：正浮点数
         */
        val REGEX_POSITIVE_FLOAT = "^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$"
        /**
         * 正则：负浮点数
         */
        val REGEX_NEGATIVE_FLOAT = "^-[1-9]\\d*\\.\\d*|-0\\.\\d*[1-9]\\d*$"
    }

}