package org.seven.khttp.body


import java.io.IOException

import okhttp3.MediaType
import okhttp3.RequestBody
import okio.Buffer
import okio.BufferedSink
import okio.ForwardingSink
import okio.Okio
import okio.Sink
import org.seven.khttp.callback.response.ProgressResponseCallBack

/**
 *
 * 上传请求体
 * Created by Seven on 2018/7/23.
 */
class UploadProgressRequestBody : RequestBody {

    protected lateinit var delegate: RequestBody
    protected var progressCallBack: ProgressResponseCallBack? = null

    protected lateinit var countingSink: CountingSink

    constructor(listener: ProgressResponseCallBack) {
        this.progressCallBack = listener
    }

    constructor(delegate: RequestBody, progressCallBack: ProgressResponseCallBack?) {
        this.delegate = delegate
        this.progressCallBack = progressCallBack
    }

    fun setRequestBody(delegate: RequestBody) {
        this.delegate = delegate
    }

    override fun contentType(): MediaType? {
        return delegate.contentType()

    }

    /**
     * 重写调用实际的响应体的contentLength
     */
    override fun contentLength(): Long {
        try {
            return delegate.contentLength()
        } catch (e: IOException) {
            e.printStackTrace()
            return -1
        }

    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        val bufferedSink: BufferedSink = Okio.buffer(countingSink)

        countingSink = CountingSink(sink)

        delegate.writeTo(bufferedSink)

        bufferedSink.flush()
    }


    protected inner class CountingSink(delegate: Sink) : ForwardingSink(delegate) {
        private var bytesWritten: Long = 0
        private var contentLength: Long = 0  //总字节长度，避免多次调用contentLength()方法
        private var lastRefreshUiTime: Long = 0  //最后一次刷新的时间

        @Throws(IOException::class)
        override fun write(source: Buffer, byteCount: Long) {
            super.write(source, byteCount)
            if (contentLength <= 0) contentLength = contentLength() //获得contentLength的值，后续不再调用
            //增加当前写入的字节数
            bytesWritten += byteCount

            val curTime = System.currentTimeMillis()
            //每100毫秒刷新一次数据,防止频繁无用的刷新
            if (curTime - lastRefreshUiTime >= 100 || bytesWritten == contentLength) {
                progressCallBack!!.onResponseProgress(bytesWritten, contentLength, bytesWritten == contentLength)
                lastRefreshUiTime = System.currentTimeMillis()
            }
        }
    }

}
