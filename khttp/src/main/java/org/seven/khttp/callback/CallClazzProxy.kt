package org.seven.khttp.callback

import com.google.gson.internal.`$Gson$Types`
import okhttp3.ResponseBody
import org.seven.khttp.model.ApiResult
import org.seven.khttp.utils.KHttpUtils
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 *
 * 提供Clazz回调代理
 * Created by Seven on 2018/7/17.
 */
abstract class CallClazzProxy<T : ApiResult<R>, R>(val callType: Type?) : IType<T> {

    override//CallClazz代理方式，获取需要解析的Type
    val type: Type
        get() {
            var typeArguments: Type? = null
            if (callType != null) {
                typeArguments = callType
            }
            if (typeArguments == null) {
                typeArguments = ResponseBody::class.java
            }
            var rawType = KHttpUtils.findNeedType(javaClass)
            if (rawType is ParameterizedType) {
                rawType = rawType.rawType
            }
            return `$Gson$Types`.newParameterizedTypeWithOwner(null, rawType, typeArguments)
        }
}
