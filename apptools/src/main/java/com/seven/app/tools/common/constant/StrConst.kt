package com.seven.app.tools.common.constant

import java.io.File
import java.nio.charset.Charset


/**
 * 字符串常量
 * Created by Seven on 2018/1/23.
 */
class StrConst private constructor() {

    init {
        throw UnsupportedOperationException("please do not init StrConst")
    }

    companion object {

        val DEFAULT_CHARSET_NAME = "UTF-8"
        val DEFAULT_CHARSET = Charset.forName(DEFAULT_CHARSET_NAME)
        val FILE_SEP = File.separator
        val FILE_PATH_SEP = File.pathSeparator
    }
}