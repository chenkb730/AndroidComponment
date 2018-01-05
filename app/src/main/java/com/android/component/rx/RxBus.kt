package com.android.component.rx

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

/**
 *
 * Created by Seven on 2018/1/5.
 */
object RxBus {

    val disposables = mutableMapOf<Any, CompositeDisposable>()

    val publishSubject = PublishSubject.create<Any>()!!


    inline fun <reified T : Any> subscribe(subscriber: Any, noinline consumer: (T) -> Unit) {
        val observer = publishSubject.ofType(T::class.java).subscribe(consumer)
        val disposable = disposables[subscriber] ?: CompositeDisposable().apply { disposables[subscriber] = this }
        disposable.add(observer)
    }


    fun unsubscribe(subscriber: Any) {
        disposables[subscriber]?.clear()
        disposables.remove(subscriber)
    }

    fun post(event: Any) {
        publishSubject.onNext(event)
    }
}