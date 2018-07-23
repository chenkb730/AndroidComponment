package org.seven.khttp.callback.response

import android.os.Handler
import android.os.Looper
import android.os.Message

import java.io.Serializable
import java.lang.ref.WeakReference

/**
 *
 * 可以直接更新UI的回调
 * Created by Seven on 2018/7/23.
 */
abstract class UIProgressResponseCallBack {

    companion object {
        private val RESPONSE_UPDATE = 0x02
    }

    //主线程Handler
    private val mHandler = UIHandler(Looper.getMainLooper(), this)

    //处理UI层的Handler子类
    private class UIHandler(looper: Looper, uiProgressResponseListener: UIProgressResponseCallBack) : Handler(looper) {
        //弱引用
        private val mUIProgressResponseListenerWeakReference: WeakReference<UIProgressResponseCallBack> = WeakReference(uiProgressResponseListener)

        override fun handleMessage(msg: Message) {
            when (msg.what) {
                RESPONSE_UPDATE -> {
                    val uiProgressResponseListener = mUIProgressResponseListenerWeakReference.get()
                    if (uiProgressResponseListener != null) {
                        //获得进度实体类
                        val progressModel = msg.obj as ProgressModel
                        //回调抽象方法
                        uiProgressResponseListener.onUIResponseProgress(progressModel.getCurrentBytes(), progressModel.getContentLength(), progressModel.isDone())
                    }
                }
                else -> super.handleMessage(msg)
            }
        }
    }

    fun onResponseProgress(bytesWritten: Long, contentLength: Long, done: Boolean) {
        //通过Handler发送进度消息
        val message = Message.obtain()
        message.obj = ProgressModel(bytesWritten, contentLength, done)
        message.what = RESPONSE_UPDATE
        mHandler.sendMessage(message)
    }

    /**
     * UI层回调抽象方法
     *
     * @param bytesRead     当前读取响应体字节长度
     * @param contentLength 总字节长度
     * @param done          是否读取完成
     */
    abstract fun onUIResponseProgress(bytesRead: Long, contentLength: Long, done: Boolean)

    inner class ProgressModel(//当前读取字节长度
            private var currentBytes: Long, //总字节长度
            private var contentLength: Long, //是否读取完成
            private var done: Boolean) : Serializable {

        fun getCurrentBytes(): Long {
            return currentBytes
        }

        fun setCurrentBytes(currentBytes: Long): ProgressModel {
            this.currentBytes = currentBytes
            return this
        }

        fun getContentLength(): Long {
            return contentLength
        }

        fun setContentLength(contentLength: Long): ProgressModel {
            this.contentLength = contentLength
            return this
        }

        fun isDone(): Boolean {
            return done
        }

        fun setDone(done: Boolean): ProgressModel {
            this.done = done
            return this
        }

        override fun toString(): String {
            return "ProgressModel{" +
                    "currentBytes=" + currentBytes +
                    ", contentLength=" + contentLength +
                    ", done=" + done +
                    '}'.toString()
        }
    }


}
