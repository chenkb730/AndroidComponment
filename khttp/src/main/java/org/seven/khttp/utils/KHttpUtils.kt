package org.seven.khttp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.os.Looper

import java.io.Closeable
import java.io.File
import java.io.IOException
import java.lang.reflect.GenericArrayType
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.TypeVariable
import java.util.Collections

import okhttp3.RequestBody

/**
 * 工具类
 *Created by Seven on 2018/7/17.
 */
object KHttpUtils {
    /**
     *检测是否是空
     */
    fun <T> checkNotNull(t: T?, message: String?): T {
        return t ?: throw NullPointerException(message)
    }

    /**
     * 是否在主线程
     */
    fun checkMain(): Boolean {
        return Thread.currentThread() === Looper.getMainLooper().thread
    }

    /**
     * 拼接请求参数
     */
    fun createUrlFromParams(url: String?, params: Map<String, String>?): String? {
        return try {
            val sb = StringBuilder()
            sb.append(url)
            if (url!!.indexOf('&', ignoreCase = true) > 0 || url.indexOf('?', ignoreCase = true) > 0) {
                sb.append("&")
            } else {
                sb.append("?")
            }

            for ((key, urlValues) in params!!) {
                sb.append(key).append("=").append(urlValues).append("&")
            }
            sb.deleteCharAt(sb.length - 1)
            sb.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }

    }

    /**
     * 创建请求的json参数
     */
    fun createJson(jsonString: String?): RequestBody? {
        checkNotNull(jsonString, "json not null!")
        return RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonString)
    }


    /**
     * 创建请求的文件
     */
    fun createFile(name: String?): RequestBody? {
        checkNotNull(name, "$name must not be  null!")
        return RequestBody.create(okhttp3.MediaType.parse("multipart/form-data; charset=utf-8"), name)
    }

    /**
     * 创建请求文件
     */
    fun createFile(file: File?): RequestBody? {
        checkNotNull(file, "${file!!.absoluteFile.name} has no found!")
        return RequestBody.create(okhttp3.MediaType.parse("multipart/form-data; charset=utf-8"), file)
    }

    /**
     * 创建请求图片
     */
    fun createImage(file: File?): RequestBody? {
        checkNotNull(file, "${file!!.absoluteFile.name} has no found!")
        return RequestBody.create(okhttp3.MediaType.parse("image/jpg; charset=utf-8"), file)
    }


    fun <R> findNeedType(cls: Class<R>): Type {
        val typeList = KHttpUtils.getMethodTypes(cls)
        return if (typeList == null || typeList.isEmpty()) {
            RequestBody::class.java
        } else typeList[0]
    }

    fun <T> getMethodTypes(cls: Class<T>): List<Type>? {
        val typeOri = cls.genericSuperclass
        var needTypes: MutableList<Type>? = null
        if (typeOri is ParameterizedType) {
            needTypes = arrayListOf()
            val parenTypes = typeOri.actualTypeArguments
            for (childType in parenTypes) {
                needTypes.add(childType)
                if (childType is ParameterizedType) {
                    val childTypes = childType.actualTypeArguments
                    Collections.addAll(needTypes, *childTypes)
                }
            }
        }
        return needTypes
    }

    fun getClass(type: Type, i: Int): Class<*>? {
        return when (type) {
            is ParameterizedType ->
                getGenericClass(type, i)
            is TypeVariable<*> -> getClass(type.bounds[0], 0)
            else -> type as Class<*>
        }
    }

    fun getType(type: Type, i: Int): Type? {
        return when (type) {
            is ParameterizedType ->
                getGenericType(type, i)
            is TypeVariable<*> -> getType(type.bounds[0], 0)
            else -> type
        }
    }

    fun getParameterizedType(type: Type, i: Int): Type? {
        return when (type) {
            is ParameterizedType -> type.actualTypeArguments[i]
            is TypeVariable<*> -> getType(type.bounds[0], 0)
            else -> type
        }
    }

    fun getGenericClass(parameterizedType: ParameterizedType, i: Int): Class<*>? {
        val genericClass = parameterizedType.actualTypeArguments[i]
        return when (genericClass) {
            is ParameterizedType -> genericClass.rawType as Class<*>
            is GenericArrayType -> genericClass.genericComponentType as Class<*>
            is TypeVariable<*> -> getClass(genericClass.bounds[0], 0)
            else -> genericClass as Class<*>
        }
    }

    fun getGenericType(parameterizedType: ParameterizedType, i: Int): Type? {
        val genericType = parameterizedType.actualTypeArguments[i]
        return when (genericType) {
            is ParameterizedType -> genericType.rawType
            is GenericArrayType -> genericType.genericComponentType
            is TypeVariable<*> -> getClass(genericType.bounds[0], 0)
            else -> genericType
        }
    }

    /**
     * 网络可用情况
     */
    fun isNetworkAvailable(context: Context): Boolean {
        val manager = context.applicationContext.getSystemService(
                Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = manager.activeNetworkInfo
        return !(null == info || !info.isAvailable)
    }


    /**
     * 普通类反射获取泛型方式，获取需要实际解析的类型
     */
    fun <T> findNeedClass(cls: Class<T>): Type {
        val genType = cls.genericSuperclass
        val params = (genType as ParameterizedType).actualTypeArguments
        val type = params[0]
        val finalNeedType: Type
        finalNeedType = if (params.size > 1) {
            if (type !is ParameterizedType) throw IllegalStateException("没有填写泛型参数")
            type.actualTypeArguments[0]
        } else {
            type
        }
        return finalNeedType
    }

    /**
     * 普通类反射获取泛型方式，获取最顶层的类型
     */
    fun <T> findRawType(cls: Class<T>): Type? {
        val genType = cls.genericSuperclass
        return getGenericType(genType as ParameterizedType, 0)
    }

    @Throws(IOException::class)
    fun closeThrowException(close: Closeable?) {
        close?.close()
    }

    fun close(close: Closeable?) {
        if (close != null) {
            try {
                closeThrowException(close)
            } catch (ignored: IOException) {
            }

        }
    }
}
