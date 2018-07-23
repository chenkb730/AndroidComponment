package org.seven.khttp.model

import android.os.Build
import android.text.TextUtils

import org.json.JSONException
import org.json.JSONObject

import java.io.Serializable
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.LinkedHashMap
import java.util.Locale
import java.util.TimeZone

/**
 *
 * 头部参数
 * Created by Seven on 2018/7/17.
 */
class KHttpHeaders constructor(private val key: String? = null, private val value: String? = null) : Serializable {

    init {
        init()
    }

    var headersMap: LinkedHashMap<String, String>? = null

    val isEmpty: Boolean
        get() = headersMap!!.isEmpty()

    val names: Set<String>
        get() = headersMap!!.keys

    private fun init() {
        headersMap = LinkedHashMap()
        put(key, value)
    }


    fun put(key: String?, value: String?) {
        if (key != null && value != null) {
            headersMap!!.remove(key)
            headersMap!![key] = value
        }
    }

    fun put(headersK: KHttpHeaders?) {
        if (headersK != null) {
            if (headersK.headersMap != null && !headersK.headersMap!!.isEmpty()) {
                val set = headersK.headersMap!!.entries
                for ((key, value) in set) {
                    headersMap!!.remove(key)
                    headersMap!![key] = value
                }
            }

        }
    }

    operator fun get(key: String?): String? {
        return headersMap!![key!!]
    }

    fun remove(key: String?): String? {
        return if (headersMap!!.containsKey(key!!)) {
            headersMap?.remove(key)
        } else ""
    }

    fun clear() {
        headersMap!!.clear()
    }


    fun isEmpty(): Boolean? {
        return null == headersMap || headersMap!!.isEmpty()
    }


    fun toJSONString(): String {
        val jsonObject = JSONObject()
        try {
            for ((key, value) in headersMap!!) {
                jsonObject.put(key, value)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return jsonObject.toString()
    }

    override fun toString(): String {
        return "KHttpHeaders{" + "headersMap=" + headersMap + '}'.toString()
    }

    companion object {
        val FORMAT_HTTP_DATA = "EEE, dd MMM y HH:mm:ss 'GMT'"
        val GMT_TIME_ZONE = TimeZone.getTimeZone("GMT")

        val HEAD_KEY_RESPONSE_CODE = "ResponseCode"
        val HEAD_KEY_RESPONSE_MESSAGE = "ResponseMessage"
        val HEAD_KEY_ACCEPT = "Accept"
        val HEAD_KEY_ACCEPT_ENCODING = "Accept-Encoding"
        val HEAD_VALUE_ACCEPT_ENCODING = "gzip, deflate"
        val HEAD_KEY_ACCEPT_LANGUAGE = "Accept-Language"
        val HEAD_KEY_CONTENT_TYPE = "Content-Type"
        val HEAD_KEY_CONTENT_LENGTH = "Content-Length"
        val HEAD_KEY_CONTENT_ENCODING = "Content-Encoding"
        val HEAD_KEY_CONTENT_DISPOSITION = "Content-Disposition"
        val HEAD_KEY_CONTENT_RANGE = "Content-Range"
        val HEAD_KEY_CACHE_CONTROL = "Cache-Control"
        val HEAD_KEY_CONNECTION = "Connection"
        val HEAD_VALUE_CONNECTION_KEEP_ALIVE = "keep-alive"
        val HEAD_VALUE_CONNECTION_CLOSE = "close"
        val HEAD_KEY_DATE = "Date"
        val HEAD_KEY_EXPIRES = "Expires"
        val HEAD_KEY_E_TAG = "ETag"
        val HEAD_KEY_PRAGMA = "Pragma"
        val HEAD_KEY_IF_MODIFIED_SINCE = "If-Modified-Since"
        val HEAD_KEY_IF_NONE_MATCH = "If-None-Match"
        val HEAD_KEY_LAST_MODIFIED = "Last-Modified"
        val HEAD_KEY_LOCATION = "Location"
        val HEAD_KEY_USER_AGENT = "User-Agent"
        val HEAD_KEY_COOKIE = "Cookie"
        val HEAD_KEY_COOKIE2 = "Cookie2"
        val HEAD_KEY_SET_COOKIE = "Set-Cookie"
        val HEAD_KEY_SET_COOKIE2 = "Set-Cookie2"
        /**
         * Accept-Language: zh-CN,zh;q=0.8
         */
        var acceptLanguage: String? = null
            get() {
                return if (TextUtils.isEmpty(field)) {
                    val locale = Locale.getDefault()
                    val language = locale.language
                    val country = locale.country
                    val acceptLanguageBuilder = StringBuilder(language)
                    if (!TextUtils.isEmpty(country))
                        acceptLanguageBuilder.append('-').append(country).append(',').append(language).append(";q=0.8")
                    this.acceptLanguage = acceptLanguageBuilder.toString()
                    field
                } else field
            }
        /**
         * User-Agent: Mozilla/5.0 (Linux; U; Android 5.0.2; zh-cn; Redmi Note 3 Build/LRX22G) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Mobile Safari/537.36
         */
        var userAgent: String? = null
            get() {
                return if (TextUtils.isEmpty(field)) {
                    var webUserAgent: String? = null
                    try {
                        val sysResCls = Class.forName("com.android.internal.R\$string")
                        val webUserAgentField = sysResCls.getDeclaredField("web_user_agent")
                        val resId = webUserAgentField.get(null) as Int
                    } catch (e: Exception) {
                    }

                    if (TextUtils.isEmpty(webUserAgent)) {
                        webUserAgent = "Mozilla/5.0 (Linux; U; Android %s) AppleWebKit/533.1 (KHTML, like Gecko) Version/5.0 %sSafari/533.1"
                    }

                    val locale = Locale.getDefault()
                    val buffer = StringBuffer()
                    val version = Build.VERSION.RELEASE
                    if (version.isNotEmpty()) {
                        buffer.append(version)
                    } else {
                        buffer.append("1.0")
                    }
                    buffer.append("; ")
                    val language = locale.language
                    if (language != null) {
                        buffer.append(language.toLowerCase(locale))
                        val country = locale.country
                        if (!TextUtils.isEmpty(country)) {
                            buffer.append("-")
                            buffer.append(country.toLowerCase(locale))
                        }
                    } else {
                        buffer.append("en")
                    }
                    if ("REL" == Build.VERSION.CODENAME) {
                        val model = Build.MODEL
                        if (model.isNotEmpty()) {
                            buffer.append("; ")
                            buffer.append(model)
                        }
                    }
                    val id = Build.ID
                    if (id.isNotEmpty()) {
                        buffer.append(" Build/")
                        buffer.append(id)
                    }
                    this.userAgent = String.format(webUserAgent!!, buffer, "Mobile ")
                    field
                } else field
            }

        fun getDate(gmtTime: String): Long {
            return try {
                parseGMTToMillis(gmtTime)
            } catch (e: ParseException) {
                0
            }

        }

        fun getDate(milliseconds: Long): String {
            return formatMillisToGMT(milliseconds)
        }

        fun getExpiration(expiresTime: String): Long {
            return try {
                parseGMTToMillis(expiresTime)
            } catch (e: ParseException) {
                -1
            }

        }

        fun getLastModified(lastModified: String): Long {
            return try {
                parseGMTToMillis(lastModified)
            } catch (e: ParseException) {
                0
            }

        }

        fun getCacheControl(cacheControl: String?, pragma: String?): String? {
            return cacheControl ?: pragma
        }

        @Throws(ParseException::class)
        fun parseGMTToMillis(gmtTime: String): Long {
            if (TextUtils.isEmpty(gmtTime)) return 0
            val formatter = SimpleDateFormat(FORMAT_HTTP_DATA, Locale.US)
            formatter.timeZone = GMT_TIME_ZONE
            val date = formatter.parse(gmtTime)
            return date.time
        }

        fun formatMillisToGMT(milliseconds: Long): String {
            val date = Date(milliseconds)
            val simpleDateFormat = SimpleDateFormat(FORMAT_HTTP_DATA, Locale.US)
            simpleDateFormat.timeZone = GMT_TIME_ZONE
            return simpleDateFormat.format(date)
        }
    }
}