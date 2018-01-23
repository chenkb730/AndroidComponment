package com.seven.app.tools.common.format

/**
 * 格式化数值化工具
 * Created by Seven on 2018/1/23.
 */
class FormatTool private constructor() {

    init {
        throw UnsupportedOperationException("please do not init FormatTool")
    }

    companion object {

        /**
         * 控制判断  true-为空  false-不为空
         */
        fun isEmpty(arg: Any?): Boolean {
            return if (arg == null) {
                return true
            } else
                trimString(arg).isEmpty()
        }


        fun toString(vararg args: Any?): String {
            return if (args != null) {
                if (args.size > 1) {
                    toString(args[args.size - 1])
                }
                val obj = args[0]
                obj.toString()
            } else {
                ""
            }
        }

        /**
         * 去空格
         */
        fun trimString(vararg args: Any?): String {
            val str = toString(*args)
            return str.replace("\\s*".toRegex(), "")
        }


        /**
         * 格式化异常信息
         */
        fun toException(throwable: Throwable?): String {
            val stackTrace = throwable?.stackTrace
            val ste = if (stackTrace != null && stackTrace.isNotEmpty()) stackTrace[0] else null
            return if (ste != null) {
                String.format("%s - [%s] [%s.%s(%s)]", throwable?.message, ste.fileName, ste.className, ste.methodName, ste.lineNumber)
            } else {
                throwable?.message!!
            }
        }
    }

}