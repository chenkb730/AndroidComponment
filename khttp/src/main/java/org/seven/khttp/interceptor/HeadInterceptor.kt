package org.seven.khttp.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.seven.khttp.model.KHttpHeaders

/**
 * 头部过滤器
 * Created by Seven on 2018/7/17.
 */
class HeadInterceptor constructor(private val KHttpHeaders: KHttpHeaders? = null) : Interceptor {
    override fun intercept(chain: Interceptor.Chain?): Response {
        val builder: Request.Builder = chain?.request()!!.newBuilder()

        if (null == KHttpHeaders || KHttpHeaders.headersMap!!.isNotEmpty()) {
            return chain.proceed(builder.build())
        }

        for ((key, value) in KHttpHeaders.headersMap!!) {
            builder.header(key, value).build()
        }

        return chain.proceed(builder.build())
    }
}