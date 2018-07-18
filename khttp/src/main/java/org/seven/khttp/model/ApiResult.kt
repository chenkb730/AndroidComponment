package org.seven.khttp.model

/**
 *
 * 提供的默认的标注返回api
 * Created by Seven on 2018/7/17.
 */
class ApiResult<T> {
    var code: Int = 0
    var msg: String? = null
    var data: T? = null

    val isSuccess: Boolean
        get() = code == 0

    override fun toString(): String {
        return "ApiResult{" +
                "code='" + code + '\''.toString() +
                ", msg='" + msg + '\''.toString() +
                ", data=" + data +
                '}'.toString()
    }
}
