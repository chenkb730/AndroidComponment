package org.seven.khttp.callback

import java.lang.reflect.Type

/**
 *
 * 获取类型接口
 * Created by Seven on 2018/7/17.
 */
interface IType<T> {
    val type: Type?
        get() = null
    val rawType: Type?
        get() = null
}
