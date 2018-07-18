package org.seven.khttp.callback

import com.google.gson.internal.`$Gson$Types`
import okhttp3.ResponseBody
import org.seven.khttp.model.ApiResult
import org.seven.khttp.utils.KHttpUtils
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * 提供回调代理
 * Created by Seven on 2018/7/17.
 */

abstract class CallBackProxy<T : ApiResult<R>, R>(private var mCallBack: CallBack<R>?) : IType<T> {

    val callBack: CallBack<R>?
        get() = mCallBack

    override val type: Type
        get() {
            var typeArguments: Type? = null
            if (mCallBack != null) {
                val rawType = mCallBack!!.rawType
                typeArguments = if (!(!List::class.java.isAssignableFrom(KHttpUtils.getClass(rawType, 0)) && !Map::class.java.isAssignableFrom(KHttpUtils.getClass(rawType, 0)))) {
                    mCallBack!!.type
                }
//                else if (CacheResult::class.java!!.isAssignableFrom(KHttpUtils.getClass(rawType, 0))) {
//                    val type = mCallBack!!.type
//                    KHttpUtils.getParameterizedType(type, 0)
//                }
                else {
                    val type = mCallBack!!.type
                    KHttpUtils.getClass(type, 0)
                }
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
