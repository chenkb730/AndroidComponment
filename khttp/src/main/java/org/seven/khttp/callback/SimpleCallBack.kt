package org.seven.khttp.callback

/**
 *
 * 简单的回调,默认可以使用该回调，不用关注其他回调方法
 * Created by Seven on 2018/7/17.
 */
abstract class SimpleCallBack<T> : CallBack<T>() {

    override fun onStart() {}

    override fun onCompleted() {

    }
}
