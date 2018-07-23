package org.seven.khttp.model

import android.text.TextUtils
import okhttp3.MediaType
import org.seven.khttp.callback.response.ProgressResponseCallBack
import java.io.File
import java.io.InputStream
import java.io.Serializable
import java.net.URLConnection
import java.util.*

/**
 * 普通参数
 * Created by Seven on 2018/7/17.
 */
class KHttpParams constructor(key: String? = null, value: String? = null) : Serializable {
    /**
     * 普通的键值对参数
     */
    var urlParamsMap: LinkedHashMap<String, String>? = null
    /**
     * 文件的键值对参数
     */
    var fileParamsMap: LinkedHashMap<String, MutableList<FileWrapper<*>>>? = null

    init {
        init()
        if (null != key && null != value) {
            put(key, value)
        }
    }


    private fun init() {
        urlParamsMap = LinkedHashMap()
        fileParamsMap = LinkedHashMap()
    }

    fun put(params: KHttpParams?) {
        if (params != null) {
            if (params.urlParamsMap != null && !params.urlParamsMap!!.isEmpty())
                urlParamsMap!!.putAll(params.urlParamsMap!!)

            if (params.fileParamsMap != null && !params.fileParamsMap!!.isEmpty()) {
                fileParamsMap!!.putAll(params.fileParamsMap!!)
            }
        }
    }

    fun put(params: Map<String, String>?) {
        if (params == null || params.isEmpty()) return
        urlParamsMap!!.putAll(params)
    }

    fun put(key: String, value: String) {
        urlParamsMap!![key] = value
    }

    fun <T : Any> put(key: String, file: T, fileName: String?, responseCallBack: ProgressResponseCallBack) {
        var name = fileName
        if (TextUtils.isEmpty(name)) {
            if (file is File) {
                name = (file as File).name
            }
        }


        put(key, file, name, guessMimeType(name!!), responseCallBack)
    }


    fun put(key: String?, fileWrapper: FileWrapper<*>?) {
        if (key != null && fileWrapper != null) {
            put(key, fileWrapper.file!!, fileWrapper.fileName, fileWrapper.contentType, fileWrapper.responseCallBack)
        }
    }

    fun <T> put(key: String?, countent: T, fileName: String?, contentType: MediaType?, responseCallBack: ProgressResponseCallBack?) {
        if (key != null) {
            var fileWrappers: MutableList<FileWrapper<*>>? = fileParamsMap!![key]
            if (fileWrappers == null) {
                fileWrappers = arrayListOf()
                fileParamsMap!![key] = fileWrappers
            }
            fileWrappers.add(FileWrapper(countent, fileName!!, contentType!!, responseCallBack!!))
        }
    }

    fun <T : File> putFileParams(key: String?, files: List<T>?, responseCallBack: ProgressResponseCallBack?) {
        if (key != null && files != null && !files.isEmpty()) {
            for (file in files) {
                put<File>(key, file, "", responseCallBack!!)
            }
        }
    }


    fun putFileWrapperParams(key: String?, fileWrappers: List<FileWrapper<*>>?) {
        if (key != null && fileWrappers != null && !fileWrappers.isEmpty()) {
            for (fileWrapper in fileWrappers) {
                put(key, fileWrapper)
            }
        }
    }

    fun removeUrl(key: String?): String? {
        return if (urlParamsMap!!.containsKey(key)) {
            urlParamsMap!!.remove(key)
        } else ""

    }

    fun removeFile(key: String?): String? {
        return if (fileParamsMap?.containsKey(key!!)!!) {
            fileParamsMap?.remove(key!!)
            "1"
        } else ""
    }

    fun remove(key: String) {
        removeUrl(key)
        removeFile(key)
    }

    fun clear() {
        urlParamsMap!!.clear()
        fileParamsMap!!.clear()
    }

    private fun guessMimeType(path: String): MediaType? {
        var path = path
        val fileNameMap = URLConnection.getFileNameMap()
        path = path.replace("#", "")   //解决文件名中含有#号异常的问题
        var contentType: String? = fileNameMap.getContentTypeFor(path)
        if (contentType == null) {
            contentType = "application/octet-stream"
        }
        return MediaType.parse(contentType)
    }

    /**
     * 文件类型的包装类
     */
    inner class FileWrapper<T>(var file: T?
                               , var fileName: String?, var contentType: MediaType?, var responseCallBack: ProgressResponseCallBack?) {
        var fileSize: Long = 0

        init {
            if (file is File) {
                this.fileSize = (file as File).length()
            } else if (file is ByteArray) {
                this.fileSize = (file as ByteArray).size.toLong()
            }
        }

        override fun toString(): String {
            return "FileWrapper{file=$file, fileName=$fileName, contentType=$contentType, fileSize=$fileSize}"
        }
    }

    override fun toString(): String {
        val result = StringBuilder()
        for ((key, value) in urlParamsMap!!) {
            if (result.isNotEmpty()) {
                result.append("&")
            }
            result.append(key).append("=").append(value)
        }
        for ((key, value) in fileParamsMap!!) {
            if (result.isNotEmpty()) {
                result.append("&")
            }
            result.append(key).append("=").append(value)
        }
        return result.toString()
    }
}