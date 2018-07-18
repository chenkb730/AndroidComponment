package org.seven.khttp.callback

/**
 *
 * 下载进度回调（主线程，可以直接操作UI）
 * Created by Seven on 2018/7/17.
 */
abstract class DownloadProgressCallBack<T> : CallBack<T>() {

    override fun onSuccess(response: T) {
    }

    abstract fun update(bytesRead: Long, contentLength: Long, done: Boolean)

    abstract fun onComplete(path: String)

    override fun onCompleted() {

    }
}
