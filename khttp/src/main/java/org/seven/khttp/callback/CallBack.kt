package org.seven.khttp.callback


import com.hazz.kotlinmvp.net.exception.ApiException
import org.seven.khttp.utils.KHttpUtils
import java.lang.reflect.Type

/**
 * 网络请求回调
 * Created by Seven on 2018/7/17.
 */
abstract class CallBack<T> : IType<T> {

    //获取需要解析的泛型T类型
    override val type: Type?
        get() = KHttpUtils.findNeedClass(javaClass)

    //获取需要解析的泛型T raw类型
    override val rawType: Type?
        get() = KHttpUtils.findRawType(javaClass)

    abstract fun onStart()

    abstract fun onCompleted()

    abstract fun onError(e: ApiException)

    abstract fun onSuccess(t: T)
}
