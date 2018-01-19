package com.seven.compoment.statuslayout

import android.view.View
import android.view.ViewGroup
import java.lang.IllegalStateException

/**
 * Created by Seven on 2018/1/16.
 */
class StatusViewHelper(view: View?) {

    var contentView: View? = null

    var currentView: View? = null

    var params: ViewGroup.LayoutParams? = null

    var parentView: ViewGroup? = null

    var viewIndex: Int = 0

    init {

        if (view == null) {
            throw IllegalStateException("status view must be not null")
        }
        contentView = view
        getContentParams()
    }

    private fun getContentParams() {
        params = contentView!!.layoutParams
        parentView = if (contentView!!.parent != null) {
            contentView!!.parent as ViewGroup
        } else {
            contentView!!.rootView.findViewById(android.R.id.content)
        }

        for (i in 0..parentView!!.childCount) {
            if (contentView == parentView!!.getChildAt(i)) {
                this.viewIndex = i
                break
            }
        }
        this.currentView = contentView
    }


    fun showStatusView(view: View?): Boolean {
        if (null == view) {
            return false
        }

        if (currentView != view) {
            currentView = view
            val parent: ViewGroup? = view.parent as ViewGroup?
            parent?.removeView(view)
            parentView?.removeViewAt(viewIndex)
            parentView?.addView(view, viewIndex, params)
            return true
        }

        return false
    }

    fun restoreStatusView() {
        showStatusView(contentView)
    }
}