package org.seven.khttp.request

import android.content.Context
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.seven.khttp.api.KApiServise
import org.seven.khttp.https.HttpsUtils
import org.seven.khttp.interceptor.HeadInterceptor
import org.seven.khttp.model.KHttpHeaders
import org.seven.khttp.model.KHttpParams
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import java.net.Proxy
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier

/**
 * Created by Seven on 2018/7/17.
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseRequest<R : BaseRequest<R>> constructor(url: String) {

    companion object {
        const val TIMEOUT = 40L
        const val RETRYTIME = 0
    }

    protected var context: Context? = null
    private var apiService: KApiServise? = null
    private var okHttpClient: OkHttpClient? = null
    private var retrofit: Retrofit? = null

    private var baseUrl: String = ""
    private var url: String = ""
    private var httpUrl: HttpUrl? = null

    private var readTimeOut = TIMEOUT
    private var writeTimeOut: Long = TIMEOUT
    private var connectTimeout: Long = TIMEOUT

    private var retryCount: Int = RETRYTIME
    private var retryDelay: Int = 0
    private var retryIncreaseDelay: Int = 0

    private var headers = KHttpHeaders()
    private var params = KHttpParams()

    private var sign = false
    private var timeStamp = false
    private var accessToken = false

    private var proxy: Proxy? = null
    private var sslParams: HttpsUtils.SSLParams? = null
    private var hostnameVerifier: HostnameVerifier? = null

    private var interceptors = mutableListOf<Interceptor>()
    private var networkInterceptors = mutableListOf<Interceptor>()


    private var converterFactories = mutableListOf<Converter.Factory>()
    private var callAdapterFactories = mutableListOf<CallAdapter.Factory>()

    init {
        this.url = url
    }

    fun baseUrl(): R {
        this.baseUrl = baseUrl
        if (!baseUrl.isEmpty()) {
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

    fun headers(headersK: KHttpHeaders): R {
        this.headers = headersK
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

    fun params(params: KHttpParams): R {
        this.params = params
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

    fun sign(sign: Boolean): R {
        this.sign = sign
        return this as R
    }

    fun timeStamp(timeStamp: Boolean): R {
        this.timeStamp = timeStamp
        return this as R
    }

    fun accessToken(accessToken: Boolean): R {
        this.accessToken = accessToken
        return this as R
    }


    fun interceptors(interceptors: MutableList<Interceptor>): R {
        this.interceptors = interceptors
        return this as R
    }

    fun clearInterceptors(): R {
        if (!interceptors.isEmpty()) {
            interceptors.clear()
        }
        return this as R
    }

    fun networkInterceptors(networkInterceptors: MutableList<Interceptor>): R {
        this.networkInterceptors = networkInterceptors
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
    fun generateOkClient(): OkHttpClient.Builder {
        val builder = OkHttpClient.Builder()

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


    fun generateRetrofit(): Retrofit.Builder? {
        val newBuilder = Retrofit.Builder()
        newBuilder.baseUrl(baseUrl)
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
}
