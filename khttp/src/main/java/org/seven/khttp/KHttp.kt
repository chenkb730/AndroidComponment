package org.seven.khttp

import android.app.Application
import io.reactivex.disposables.CompositeDisposable
import okhttp3.ConnectionPool
import okhttp3.CookieJar
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.seven.khttp.https.HttpsUtils
import org.seven.khttp.model.KHttpHeaders
import org.seven.khttp.model.KHttpParams
import org.seven.khttp.utils.KHttpUtils
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.InputStream
import java.net.Proxy
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession

/**
 * 请求入口
 * Created by Seven on 2018/7/18.
 */
internal class KHttp private constructor() {


    companion object {
        private var application: Application? = null

        val INSTANCE by lazy { KHttp() }

        const val DEFAULTREADTIMEOUT = 40L
        const val DEFAULTWRITRTIMEOUT = 40L
        const val DEFAULTCONNECTTIMEOUT = 40L

        const val DEFAULTRETRY = 0
        const val DEFAULEDELAY = 30
        const val DEFAULEINCREASEDELAY = 30


        fun init(application: Application) {
            this.application = application
        }
    }

    var okHttpClientBuilder: OkHttpClient.Builder? = null
    var retrofitBuilder: Retrofit.Builder? = null


    var httpHeaders: KHttpHeaders? = null
    var httpParams: KHttpParams? = null

    var baseUrl: String? = null
    var retryCount = DEFAULTRETRY
    var retryDelay = DEFAULEDELAY
    var retryIncreaseDelay = DEFAULEINCREASEDELAY

    var cookieJar: CookieJar? = null

    init {
        okHttpClientBuilder = OkHttpClient.Builder()
        okHttpClientBuilder?.hostnameVerifier(DefaultHostnameVerifier())
        okHttpClientBuilder?.readTimeout(DEFAULTREADTIMEOUT, TimeUnit.SECONDS)
        okHttpClientBuilder?.writeTimeout(DEFAULTWRITRTIMEOUT, TimeUnit.SECONDS)
        okHttpClientBuilder?.connectTimeout(DEFAULTCONNECTTIMEOUT, TimeUnit.SECONDS)
        retrofitBuilder = Retrofit.Builder()
    }


    /**
     * 检测是否已经设置过上下文对象
     */
    private fun checkAPP() {
        if (null == application) {
            throw RuntimeException("please init the application with init() first!!")
        }
    }

    /**
     * 返回上下文对象
     */
    fun getContext(): Application? {
        checkAPP()
        return application
    }

    /**
     * 此类是用于主机名验证的基接口。 在握手期间，如果 URL 的主机名和服务器的标识主机名不匹配，
     * 则验证机制可以回调此接口的实现程序来确定是否应该允许此连接。策略可以是基于证书的或依赖于其他验证方案。
     * 当验证 URL 主机名使用的默认规则失败时使用这些回调。如果主机名是可接受的，则返回 true
     */
    inner class DefaultHostnameVerifier : HostnameVerifier {
        override fun verify(hostname: String, session: SSLSession) = true
    }

    /**
     * https的全局访问规则
     */
    fun setHostnameVerifier(hostnameVerifier: HostnameVerifier): KHttp {
        okHttpClientBuilder!!.hostnameVerifier(hostnameVerifier)
        return this
    }

    /**
     * https的全局自签名证书
     */
    fun setCertificates(certificates: Array<out InputStream>?): KHttp {
        val sslParams = HttpsUtils.getSslSocketFactory(null, null, certificates!!)
        okHttpClientBuilder!!.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
        return this
    }

    /**
     * https双向认证证书
     */
    fun setCertificates(bksFile: InputStream, password: String, certificates: Array<out InputStream>?): KHttp {
        val sslParams = HttpsUtils.getSslSocketFactory(bksFile, password, certificates!!)
        okHttpClientBuilder!!.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
        return this
    }

    /**
     * 全局cookie存取规则
     */
    fun setCookieStore(cookieJar: CookieJar?): KHttp {
        this.cookieJar = cookieJar
        okHttpClientBuilder!!.cookieJar(cookieJar!!)
        return this
    }


    /**
     * 全局读取超时时间
     */
    fun setReadTimeOut(readTimeOut: Long = DEFAULTREADTIMEOUT): KHttp {
        okHttpClientBuilder!!.readTimeout(readTimeOut, TimeUnit.MILLISECONDS)
        return this
    }

    /**
     * 全局写入超时时间
     */
    fun setWriteTimeOut(writeTimeout: Long = DEFAULTWRITRTIMEOUT): KHttp {
        okHttpClientBuilder!!.writeTimeout(writeTimeout, TimeUnit.MILLISECONDS)
        return this
    }

    /**
     * 全局连接超时时间
     */
    fun setConnectTimeout(connectTimeout: Long = DEFAULTCONNECTTIMEOUT): KHttp {
        okHttpClientBuilder!!.connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
        return this
    }

    /**
     * 超时重试次数
     */
    fun setRetryCount(retryCount: Int = DEFAULTRETRY): KHttp {
        if (retryCount < 0) throw IllegalArgumentException("retryCount must > 0")
        this.retryCount = retryCount
        return this
    }


    /**
     * 超时重试延迟时间
     */
    fun setRetryDelay(retryDelay: Int = DEFAULEDELAY): KHttp {
        if (retryDelay < 0) throw IllegalArgumentException("retryDelay must > 0")
        this.retryDelay = retryDelay
        return this
    }


    /**
     * 超时重试延迟叠加时间
     */
    fun setRetryIncreaseDelay(retryIncreaseDelay: Int = DEFAULEINCREASEDELAY): KHttp {
        if (retryIncreaseDelay < 0)
            throw IllegalArgumentException("retryIncreaseDelay must > 0")
        this.retryIncreaseDelay = retryIncreaseDelay
        return this
    }

    /**
     * 添加全局公共请求参数
     */
    fun addHttpParams(httpParam: KHttpParams?): KHttp {
        this.httpParams!!.put(httpParam ?: KHttpParams())
        return this
    }

    /**
     * 添加全局公共请求参数
     */
    fun addHttpHeaders(httpHeader: KHttpHeaders?): KHttp {
        this.httpHeaders!!.put(httpHeader ?: KHttpHeaders())
        return this
    }

    /**
     * 添加全局拦截器
     */
    fun addInterceptor(interceptor: Interceptor?): KHttp {
        okHttpClientBuilder!!.addInterceptor(KHttpUtils.checkNotNull(interceptor, "interceptor == null"))
        return this
    }

    /**
     * 添加全局网络拦截器
     */
    fun addNetworkInterceptor(interceptor: Interceptor?): KHttp {
        okHttpClientBuilder!!.addNetworkInterceptor(KHttpUtils.checkNotNull(interceptor, "networkInterceptor == null"))
        return this
    }

    /**
     * 全局设置代理
     */
    fun setOkproxy(proxy: Proxy?): KHttp {
        okHttpClientBuilder!!.proxy(KHttpUtils.checkNotNull(proxy, "proxy == null"))
        return this
    }

    /**
     * 全局设置请求的连接池
     */
    fun setOkconnectionPool(connectionPool: ConnectionPool?): KHttp {
        okHttpClientBuilder!!.connectionPool(KHttpUtils.checkNotNull(connectionPool, "connectionPool == null"))
        return this
    }

    /**
     * 全局为Retrofit设置自定义的OkHttpClient
     */
    fun setOkclient(okHttpClient: OkHttpClient?): KHttp {
        retrofitBuilder!!.client(KHttpUtils.checkNotNull(okHttpClient, "OkHttpClient == null"))
        return this
    }

    /**
     * 全局设置Converter.Factory,默认GsonConverterFactory.create()
     */
    fun addConverterFactory(factory: Converter.Factory?): KHttp {
        retrofitBuilder!!.addConverterFactory(KHttpUtils.checkNotNull(factory, "factory == null"))
        return this
    }

    /**
     * 全局设置CallAdapter.Factory,默认RxJavaCallAdapterFactory.create()
     */
    fun addCallAdapterFactory(factory: CallAdapter.Factory?): KHttp {
        retrofitBuilder!!.addCallAdapterFactory(KHttpUtils.checkNotNull(factory, "CallAdapterFactory == null"))
        return this
    }

    /**
     * 全局设置Retrofit callbackExecutor
     */
    fun setCallbackExecutor(executor: Executor?): KHttp {
        retrofitBuilder!!.callbackExecutor(KHttpUtils.checkNotNull(executor, "executor == null"))
        return this
    }

    /**
     * 全局设置Retrofit对象Factory
     */
    fun setCallFactory(factory: okhttp3.Call.Factory?): KHttp {
        retrofitBuilder!!.callFactory(KHttpUtils.checkNotNull(factory, "CallFactory == null"))
        return this
    }

    /**
     * 全局设置baseurl
     */
    fun setBaseUrl(baseUrl: String): KHttp {
        this.baseUrl = KHttpUtils.checkNotNull(baseUrl, "baseUrl == null")
        return this
    }

    /**
     * 取消请求
     */
    fun cancelDisponse(disposable: CompositeDisposable?) {
        if (!disposable!!.isDisposed) {
            disposable.dispose()
        }
    }


}