package com.hazz.kotlinmvp.net

/**
 * Created by Seven on 2018/1/4.
 * 封装返回的数据,自行修改code,msg
 */
class BaseResponse<T>(val code: Int,
                      val msg: String,
                      val data: T)