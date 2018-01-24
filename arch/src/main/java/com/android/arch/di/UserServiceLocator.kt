package com.android.arch.di

import com.android.arch.model.UserDataSource
import com.android.arch.model.UserRepository

/**
 * Created by Seven on 2018/1/24.
 */
object UserServiceLocator {

    private val userSource: UserDataSource = UserDataSource()

    val userRepository = UserRepository(userSource)
}