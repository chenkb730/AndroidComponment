package com.android.arch.model

/**
 * Created by Seven on 2018/1/24.
 */
class UserDataSource {


    val allUsers: List<User>
        get() = mutableListOf(
                createUser("Tom"),
                createUser("Jerry"),
                createUser("Kerry"),
                createUser("Jim")
        )


    private fun createUser(name: String): User {
        return User(name)
    }
}