package org.seven.khttp.request

import android.text.TextUtils
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.seven.khttp.body.RequestBodyUtils
import org.seven.khttp.body.UploadProgressRequestBody
import org.seven.khttp.callback.response.ProgressResponseCallBack
import org.seven.khttp.model.KHttpParams.FileWrapper
import org.seven.khttp.utils.KHttpUtils
import java.io.File
import java.io.InputStream

/**
 * Created by Seven on 2018/7/23.
 */
@Suppress("UNCHECKED_CAST")
open class BasePostRequest<R : BasePostRequest<R>>(url: String) : BaseRequest<R>(url) {

    enum class UploadType {
        BODY,
        PART
    }

    protected var postString: String? = null//上传的文本内容
    protected var mediaType: MediaType? = null//上传的文本内容
    protected var postJsonString: String? = null//上传的Json
    protected var postBytes: ByteArray? = null //上传的字节数据
    protected var postAny: Any? = null //上传的对象
    protected var requestBody: RequestBody? = null//请求的对象
    protected var uploadType: UploadType? = null


    fun postString(postString: String?, mediaType: MediaType = MediaType.parse("text/plain")!!): R {
        this.postString = postString
        this.mediaType = mediaType
        return this as R
    }


    fun postJsonStrinh(postJsonString: String): R {
        this.postJsonString = postJsonString
        return this as R
    }

    fun postBytes(postBytes: ByteArray?): R {
        this.postBytes = postBytes
        return this as R
    }

    fun postAny(postAny: Any?): R {
        this.postAny = postAny
        return this as R
    }

    fun uploadType(uploadType: UploadType): R {
        this.uploadType = uploadType
        return this as R
    }

    fun <T : Any> params(key: String?, t: T?, fileName: String = "", responseCallBack: ProgressResponseCallBack?): R {
        params.put(key!!, t!!, fileName, responseCallBack!!)
        return this as R
    }


    fun <T : File> fileParams(key: String?, files: List<T>, responseCallBack: ProgressResponseCallBack?): R {
        params.putFileParams(key, files, responseCallBack)
        return this as R
    }

    fun fileWrapperParams(key: String?, fileWrappers: List<FileWrapper<*>>?): R {
        params.putFileWrapperParams(key, fileWrappers)
        return this as R

    }


    fun uploadFileWithPart(): Observable<ResponseBody>? {
        val parts = mutableListOf<MultipartBody.Part>()
        for ((key, value) in params.urlParamsMap!!) {
            parts.add(MultipartBody.Part.createFormData(key, value))
        }

        for ((key, value) in params.fileParamsMap!!) {
            for (file in value) {
                parts.add(addFile(key, file))
            }
        }
        return apiService!!.uploadFiles(url, parts)
    }

    fun uploadFileWithBody(): Observable<ResponseBody>? {
        val mBodyMap = mutableMapOf<String, RequestBody>()
        //拼接参数键值对
        for ((key, value) in params.urlParamsMap!!) {
            val body = RequestBody.create(MediaType.parse("text/plain"), value)
            mBodyMap[key] = body
        }
        //拼接文件
        for ((key, value) in params.fileParamsMap!!) {
            for (fileWrapper in value) {
                val requestBody = getRequestBody(fileWrapper)
                mBodyMap[key] = UploadProgressRequestBody(requestBody!!, fileWrapper.responseCallBack)
            }
        }
        return apiService!!.uploadFiles(url, mBodyMap)
    }

    private fun addFile(key: String, fileWrapper: FileWrapper<*>): MultipartBody.Part {
        val requestBody = getRequestBody(fileWrapper)
        KHttpUtils.checkNotNull(requestBody, "requestBody==null fileWrapper.file must is File/InputStream/byte[]")
        return if (fileWrapper.responseCallBack != null) {
            val uploadProgressRequestBody = UploadProgressRequestBody(requestBody!!, fileWrapper.responseCallBack)
            MultipartBody.Part.createFormData(key, fileWrapper.fileName, uploadProgressRequestBody)
        } else {
            MultipartBody.Part.createFormData(key, fileWrapper.fileName, requestBody!!)
        }
    }

    private fun getRequestBody(fileWrapper: FileWrapper<*>): RequestBody? {
        var requestBody: RequestBody? = null
        when {
            fileWrapper.file is File -> requestBody = RequestBody.create(fileWrapper.contentType, fileWrapper.file as File)
            fileWrapper.file is InputStream -> //requestBody = RequestBodyUtils.create(RequestBodyUtils.MEDIA_TYPE_MARKDOWN, (InputStream) fileWrapper.file);
                requestBody = RequestBodyUtils.create(fileWrapper.contentType!!, fileWrapper.file as InputStream)
            fileWrapper.file is ByteArray -> {
                requestBody = RequestBody.create(fileWrapper.contentType, fileWrapper.file as ByteArray)
            }
        }
        return requestBody
    }

    override fun request(): Observable<ResponseBody>? {//body方式上传
        return when {
            this.requestBody != null ->
                apiService!!.post(url, this.requestBody)
            this.postJsonString != null -> {
                val body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), postJsonString)
                apiService!!.postJson(url, body)
            }
            this.postAny != null -> //自定义的请求object
                apiService!!.post(url, postAny)
            !TextUtils.isEmpty(postString) -> {
                val body = RequestBody.create(mediaType, postString)
                apiService!!.post(url, body)
            }
            this.postBytes != null -> {//上传的字节数据
                val body = RequestBody.create(okhttp3.MediaType.parse("application/octet-stream"), postBytes)
                apiService!!.post(url, body)
            }
            else -> return if (params.fileParamsMap!!.isEmpty()) {
                apiService!!.post(url, params.urlParamsMap)
            } else {
                return when (uploadType) {
                    UploadType.PART -> uploadFileWithPart()
                    else -> uploadFileWithBody()
                }
            }
        }
    }


}