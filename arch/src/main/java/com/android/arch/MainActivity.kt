package com.android.arch

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.android.arch.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)

        getWord.setOnClickListener({
            userViewModel.getUsers().observe(this, Observer {
                text.text = it.toString()
            })
        })

    }


}
