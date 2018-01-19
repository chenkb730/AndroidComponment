package com.android.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import com.android.component.R
import com.seven.compoment.statuslayout.StatusView
import com.seven.compoment.statuslayout.StatusViewClickListener

/**
 * Created by Seven on 2018/1/19.
 */

class StatusViewActivity : AppCompatActivity() {

    private var statusView: StatusView? = null

    private var parentView: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statusview)

        parentView = findViewById(R.id.content)
        setupStatusView()
    }


    private fun setupStatusView() {
        statusView = StatusView.StatusViewBuilder(parentView!!)
                .isEmptyClickViewVisible(true)
                .isErrorClickViewVisible(true)
                .setErrorLayout(R.layout.layout_status_view_error)
                .setLoadingText("11111")
                .setErrorText("222222")
                .setEmptyText("33333333")
                .setErrorClickText("点击重试")
                .setEmptyClickText("点击重试")
                .setOnStatusViewClickListener(object : StatusViewClickListener {
                    override fun onStatusViewEmptyClick(view: View) {
                        statusView!!.showError()
                    }

                    override fun onStatusViewErrorClick(view: View) {
                        statusView!!.showLoading()
                    }

                    override fun onStatusCustomerClick(view: View) {
                    }
                })
                .build()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.status_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.menu_status_loading -> {
                statusView!!.showLoading()
            }
            R.id.menu_status_empty
            -> {
                statusView!!.showEmpty()
            }
            R.id.menu_status_error
            -> {
                statusView!!.showError()
            }
            R.id.menu_status_success
            -> {
                statusView!!.showSuccess()
            }
            R.id.menu_status_customer
            -> {
            }
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
