package org.seven.khttp.request

import android.content.Context
import android.text.TextUtils
import io.reactivex.Observable
import okhttp3.*
import org.seven.khttp.KHttp
import org.seven.khttp.api.KApiServise
import org.seven.khttp.https.HttpsUtils
import org.seven.khttp.interceptor.HeadInterceptor
import org.seven.khttp.model.KHttpHeaders
import org.seven.khttp.model.KHttpParams
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import java.net.Proxy
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier

/**
 * Created by Seven on 2018/7/17.
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseRequest<R : BaseRequest<R>> constructor(url: String = "") {

    companion object {
        const val TIMEOUT = 40L
        const val RETRYTIME = 0
    }

    private var context: Context? = null
    protected var apiService: KApiServise? = null
    private var okHttpClient: OkHttpClient? = null
    private var retrofit: Retrofit? = null

    private var baseUrl: String = ""
    var url: String = ""
    private var httpUrl: HttpUrl? = null

    private var readTimeOut = TIMEOUT
    private var writeTimeOut: Long = TIMEOUT
    private var connectTimeout: Long = TIMEOUT

    private var retryCount: Int = RETRYTIME
    private var retryDelay: Int = 0
    private var retryIncreaseDelay: Int = 0

    protected var headers = KHttpHeaders()
    protected var params = KHttpParams()

    protected var cookies: MutableList<Cookie> = ArrayList()

    protected var proxy: Proxy? = null
    protected var sslParams: HttpsUtils.SSLParams? = null
    protected var hostnameVerifier: HostnameVerifier? = null

    protected var interceptors = mutableListOf<Interceptor>()
    protected var networkInterceptors = mutableListOf<Interceptor>()


    protected var converterFactories = mutableListOf<Converter.Factory>()
    protected var callAdapterFactories = mutableListOf<CallAdapter.Factory>()

    init {

        this.url = url

        val kHttp = KHttp.INSTANCE
        context = kHttp.getContext()

        retryCount = kHttp.retryCount
        retryDelay = kHttp.retryDelay
        retryIncreaseDelay = kHttp.retryIncreaseDelay

        baseUrl = kHttp.baseUrl!!

        if (!TextUtils.isEmpty(baseUrl)) {
            httpUrl = HttpUrl.parse(baseUrl)
        }

        //默认添加 Accept-Language
        val acceptLanguage = KHttpHeaders.acceptLanguage
        if (!TextUtils.isEmpty(acceptLanguage)) {
            headers.put(KHttpHeaders.HEAD_KEY_ACCEPT_LANGUAGE, acceptLanguage!!)
        }
        //默认添加 User-Agent
        val userAgent = KHttpHeaders.userAgent
        if (!TextUtils.isEmpty(userAgent)) {
            headers.put(KHttpHeaders.HEAD_KEY_USER_AGENT, userAgent)
        }

        //添加通用头参数
        headers(kHttp.httpHeaders!!)
        //添加通用参数
        params(kHttp.httpParams!!)

    }

    fun baseUrl(): R {
        this.baseUrl = baseUrl
        if (!TextUtils.isEmpty(baseUrl)) {
            httpUrl = HttpUrl.parse(baseUrl)
        }

        return this as R
    }

    fun readTimeOut(readTimeOut: Long): R {
        this.readTimeOut = readTimeOut
        return this as R
    }

    fun writeTimeOut(writeTimeOut: Long): R {
        this.writeTimeOut = writeTimeOut
        return this as R
    }

    fun connectTimeout(connectTimeout: Long): R {
        this.connectTimeout = connectTimeout
        return this as R
    }

    fun retryCount(retryCount: Int): R {
        this.retryCount = retryCount
        return this as R
    }

    fun retryDelay(retryDelay: Int): R {
        this.retryDelay = retryDelay
        return this as R
    }

    fun retryIncreaseDelay(retryIncreaseDelay: Int): R {
        this.retryIncreaseDelay = retryIncreaseDelay
        return this as R
    }

    fun headers(headers: KHttpHeaders?): R {
        if (null != headers) {
            this.headers.put(headers)
        }
        return this as R
    }

    fun removeHead(key: String?): R {
        headers.remove(key!!)
        return this as R
    }

    fun removeAllHeads(): R {
        headers.clear()
        return this as R
    }

    fun params(params: KHttpParams?): R {
        if (null != params) {
            this.params.put(params)
        }
        return this as R
    }

    fun removeParam(key: String?): R {
        params.remove(key!!)
        return this as R
    }

    fun removeAllParams(): R {
        params.clear()
        return this as R
    }

    fun addInterceptors(interceptor: Interceptor): R {
        interceptors.add(interceptor)
        return this as R
    }

    fun clearInterceptors(): R {
        if (!interceptors.isEmpty()) {
            interceptors.clear()
        }
        return this as R
    }

    fun addNetworkInterceptor(networkInterceptor: Interceptor): R {
        networkInterceptors.add(networkInterceptor)
        return this as R
    }

    fun clearNetworkInterceptors(): R {
        if (!networkInterceptors.isEmpty()) {
            networkInterceptors.clear()
        }
        return this as R
    }

    fun converterFactories(converterFactories: MutableList<Converter.Factory>): R {
        this.converterFactories = converterFactories
        return this as R
    }

    fun clearConverterFactories(): R {
        if (!converterFactories.isEmpty()) {
            callAdapterFactories.clear()
        }
        return this as R
    }

    fun callAdapterFactories(callAdapterFactories: MutableList<CallAdapter.Factory>): R {
        this.callAdapterFactories = callAdapterFactories
        return this as R
    }

    fun clearCallAdapterFactories(): R {
        if (!callAdapterFactories.isEmpty()) {
            callAdapterFactories.clear()
        }
        return this as R
    }


    /**
     * 根据形参创建OkHttpClient
     */
    private fun generateOkClient(): OkHttpClient.Builder {

        //默认builder
        var builder = KHttp.INSTANCE.okHttpClientBuilder!!

        if (readTimeOut > 0) {
            builder.readTimeout(readTimeOut, TimeUnit.SECONDS)
        }
        if (connectTimeout > 0) {
            builder.connectTimeout(connectTimeout, TimeUnit.SECONDS)
        }

        if (writeTimeOut > 0) {
            builder.writeTimeout(writeTimeOut, TimeUnit.SECONDS)
        }


        if (null != hostnameVerifier) {
            builder.hostnameVerifier(hostnameVerifier!!)
        }
        if (null != sslParams) {
            builder.sslSocketFactory(sslParams?.sSLSocketFactory!!, sslParams?.trustManager!!)
        }

        if (null != proxy) {
            builder.proxy(proxy)
        }

        builder.addInterceptor(HeadInterceptor(headers))

        if (!interceptors.isEmpty()) {
            for (interceptor in interceptors) {
                builder.addInterceptor(interceptor)
            }
        }

        if (!networkInterceptors.isEmpty()) {
            for (networkInterceptor in networkInterceptors) {
                builder.addNetworkInterceptor(networkInterceptor)
            }
        }
        return builder
    }


    private fun generateRetrofit(): Retrofit.Builder? {
        val newBuilder = KHttp.INSTANCE.retrofitBuilder
        newBuilder!!.baseUrl(baseUrl)
        if (!converterFactories.isEmpty()) {
            for (converterFactory in converterFactories) {
                newBuilder.addConverterFactory(converterFactory)
            }
        }

        if (!callAdapterFactories.isEmpty()) {
            for (callAdapterFactory in callAdapterFactories) {
                newBuilder.addCallAdapterFactory(callAdapterFactory)
            }
        }
        return newBuilder
    }


    fun build(): R {
        okHttpClient = generateOkClient().build()
        val builder = generateRetrofit()
        builder!!.client(okHttpClient)
        retrofit = builder.build()
        apiService = retrofit!!.create(KApiServise::class.java)
        return this as R
    }


    protected abstract fun request(): Observable<ResponseBody>?
}
