package org.seven.khttp.callback

import android.app.Dialog
import com.hazz.kotlinmvp.net.exception.ApiException
import io.reactivex.disposables.Disposable
import org.seven.khttp.subsciber.IProgressDialog
import org.seven.khttp.subsciber.ProgressCancelListener

/**
 *
 * 可以自定义带有加载进度框的回调
 * Created by Seven on 2018/7/17.
 */
abstract class ProgressDialogCallBack<T>(progressDialog: IProgressDialog,
                                         private var isShowProgress: Boolean = true, isCancel: Boolean = true) : CallBack<T>(), ProgressCancelListener {
    private var progressDialog: IProgressDialog? = progressDialog
    private var mDialog: Dialog? = null

    private var disposed: Disposable? = null


    init {
        init(isCancel)
    }

    /**
     * 初始化
     *
     * @param isCancel
     */
    private fun init(isCancel: Boolean) {
        if (progressDialog == null) return
        mDialog = progressDialog!!.dialog
        if (mDialog == null) return
        mDialog!!.setCancelable(isCancel)
        if (isCancel) {
            mDialog!!.setOnCancelListener { this@ProgressDialogCallBack.onCancelProgress() }
        }
    }

    /**
     * 展示进度框
     */
    private fun showProgress() {
        if (!isShowProgress) {
            return
        }
        if (mDialog != null) {
            if (!mDialog!!.isShowing) {
                mDialog!!.show()
            }
        }
    }

    /**
     * 取消进度框
     */
    private fun dismissProgress() {
        if (!isShowProgress) {
            return
        }
        if (mDialog != null) {
            if (mDialog!!.isShowing) {
                mDialog!!.dismiss()
            }
        }
    }

    override fun onStart() {
        showProgress()
    }

    override fun onCompleted() {
        dismissProgress()
    }

    override fun onError(e: ApiException) {
        dismissProgress()
    }

    override fun onCancelProgress() {
        if (disposed != null && !disposed!!.isDisposed) {
            disposed!!.dispose()
        }
    }

    fun subscription(disposed: Disposable) {
        this.disposed = disposed
    }
}
