package org.seven.khttp.subsciber

import android.content.Context
import com.hazz.kotlinmvp.net.exception.ApiException
import com.hazz.kotlinmvp.net.exception.ExceptionHandle
import io.reactivex.subscribers.DisposableSubscriber
import org.seven.khttp.utils.KHttpUtils
import java.lang.ref.WeakReference

/**
 * Created by Seven on 2018/7/17.
 */
abstract class BaseSubsciber<T> constructor(context: Context?) : DisposableSubscriber<T>() {

    private var contextWeakReference: WeakReference<Context>? = null

    init {
        if (null != context) {
            contextWeakReference = WeakReference(context)
        }
    }

    override fun onStart() {
        super.onStart()
        if (null == contextWeakReference || null == contextWeakReference?.get() ||
                !KHttpUtils.isNetworkAvailable(contextWeakReference!!.get())) {
            //没有网络的时候强制结束
            onComplete()
        }
    }

    override fun onError(throwable: Throwable?) {
        if (throwable is ApiException) {
            onError(throwable)
        } else {
            onError(ExceptionHandle.handleException(throwable!!))
        }
    }


    abstract fun onError(apiException: ApiException?)
}