package com.android.arch.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.android.arch.di.UserServiceLocator
import com.android.arch.model.User

/**
 * Created by Seven on 2018/1/24.
 */
class UserViewModel : ViewModel() {

    private val userRepository = UserServiceLocator.userRepository
    private val userDataLive = MutableLiveData<List<User>>()

    fun getUsers(): MutableLiveData<List<User>> {
        //同步发送
        //userDataLive.postValue(userRepository.receiveUserList())
        //异步发送
        userRepository.receiveUserList { it -> userDataLive.postValue(it) }
        return userDataLive
    }
}