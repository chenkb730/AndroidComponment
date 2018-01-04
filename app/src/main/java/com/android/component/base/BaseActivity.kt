package com.android.component.base

import android.support.v7.app.AppCompatActivity

import java.util.concurrent.Future

/**
 * Created by Seven on 2018/1/4.
 */

abstract class BaseActivity : AppCompatActivity() {

    abstract fun getLayout(): Int

    abstract fun initView()

    abstract fun initData()


}
