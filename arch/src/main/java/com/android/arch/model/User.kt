package com.android.arch.model

import java.util.*

/**
 * Created by Seven on 2018/1/24.
 */
data class User constructor(val name: String, val age: Int = Random().nextInt(100)) {


}