package com.seven.compoment.statuslayout

import android.view.View

/**
 * Created by Seven on 2018/1/16.
 */
interface StatusViewClickListener {


    fun onStatusViewEmptyClick(view: View)

    fun onStatusViewErrorClick(view: View)

    fun onStatusCustomerClick(view: View)
}