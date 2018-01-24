package com.android.arch.model

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by Seven on 2018/1/24.
 */

typealias UserCommend = (List<User>) -> Unit

class UserRepository constructor(private val userDataSource: UserDataSource) {

    companion object {
        private const val PERIOD_SECONDS: Long = 2
        private const val MIN_INDEX = 0
    }

    /**
     * 异步
     */
    fun receiveUserList(userCommend: UserCommend): Disposable {
        return Observable.timer(PERIOD_SECONDS, TimeUnit.SECONDS)
                .subscribe({
                    val userList = userDataSource.allUsers
                    var randomMax = Random().nextInt(userList.size)
                    when (randomMax) {
                        MIN_INDEX -> randomMax += 1
                    }
                    userCommend.invoke(userList.subList(MIN_INDEX, randomMax))

                }, {
                    it.printStackTrace()
                })
    }

    /**
     * 同步
     */
    fun receiveUserList(): List<User> {
        val userList = userDataSource.allUsers
        var randomMax = Random().nextInt(userList.size)
        when (randomMax) {
            MIN_INDEX -> randomMax += 1
        }

        return userList.subList(MIN_INDEX, randomMax)
    }

}