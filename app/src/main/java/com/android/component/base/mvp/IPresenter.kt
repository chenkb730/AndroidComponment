package com.hazz.kotlinmvp.base


/**
 *
 * Created by Seven on 2018/01/04.
 */


interface IPresenter<in V : IBaseView> {

    fun attachView(mRootView: V)

    fun detachView()

}
