package org.seven.khttp.body

import java.io.IOException
import java.io.InputStream

import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.internal.Util
import okio.BufferedSink
import okio.Okio
import okio.Source

/**
 *
 * 描述：请求体处理工具类
 * Created by Seven on 2018/7/23.
 */
object RequestBodyUtils {
    fun create(mediaType: MediaType, inputStream: InputStream): RequestBody {
        return object : RequestBody() {
            override fun contentType(): MediaType? {
                return mediaType
            }

            override fun contentLength(): Long {
                return try {
                    inputStream.available().toLong()
                } catch (e: IOException) {
                    0
                }

            }

            @Throws(IOException::class)
            override fun writeTo(sink: BufferedSink) {
                var source: Source? = null
                try {
                    source = Okio.source(inputStream)
                    sink.writeAll(source)
                } finally {
                    Util.closeQuietly(source)
                }
            }
        }
    }
}
