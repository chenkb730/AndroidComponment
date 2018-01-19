package com.seven.compoment.statuslayout

import android.support.annotation.DrawableRes
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.annotation.NonNull
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView

/**
 * Created by Seven on 2018/1/18.
 */
class StatusView(builder: StatusViewBuilder) {

    //==================content====================//
    private var contentLayout: View? = null


    //==================loading====================//
    private var loadingLayout: View? = null
    @LayoutRes
    private var loadingLayoutId: Int = 0
    private var loadingText: String? = ""

    //==================empty=====================//
    private var emptyView: View? = null
    @IdRes
    private var emptyClickViewId: Int = 0
    @LayoutRes
    private var emptyLayoutId: Int = 0
    private var emptyText: String? = ""
    private var emptyClickViewText: String? = ""
    private var emptyClickViewTextColor: Int? = 0
    private var isEmptyClickViewVisible: Boolean? = false
    @DrawableRes
    private var emptyImgId: Int = 0

    //==================error=====================//
    private var errorLayout: View? = null
    @IdRes
    private var errorClickViewId: Int = 0
    @LayoutRes
    private var errorLayoutId: Int = 0
    private var errorText: String? = ""
    private var errorClickViewText: String? = ""
    private var errorClickViewTextColor: Int = 0
    private var isErrorClickViewVisible: Boolean? = false
    @DrawableRes
    private var errorImgId: Int = 0

    private var onStatusViewClickListener: StatusViewClickListener? = null

    private var statusViewHelper: StatusViewHelper? = null

    private var inflater: LayoutInflater? = null

    init {
        this.contentLayout = builder.contentLayout
        this.loadingLayout = builder.loadingLayout
        this.loadingLayoutId = builder.loadingLayoutId
        this.loadingText = builder.loadingText
        this.emptyView = builder.emptyView
        this.emptyClickViewId = builder.emptyClickViewId
        this.emptyLayoutId = builder.emptyLayoutId
        this.emptyText = builder.emptyText
        this.emptyClickViewText = builder.emptyClickViewText
        this.emptyClickViewTextColor = builder.emptyClickViewTextColor
        this.isEmptyClickViewVisible = builder.isEmptyClickViewVisible
        this.emptyImgId = builder.emptyImgId
        this.errorLayout = builder.errorLayout
        this.errorClickViewId = builder.errorClickViewId
        this.errorLayoutId = builder.errorLayoutId
        this.errorText = builder.errorText
        this.errorClickViewText = builder.errorClickViewText
        this.errorClickViewTextColor = builder.errorClickViewTextColor
        this.isErrorClickViewVisible = builder.isErrorClickViewVisible
        this.errorImgId = builder.errorImgId
        this.onStatusViewClickListener = builder.onStatusViewClickListener


        statusViewHelper = StatusViewHelper(contentLayout!!)
        inflater = LayoutInflater.from(contentLayout!!.context)
    }

    private fun inflter(@LayoutRes id: Int): View {
        if (null == inflater) {
            inflater = LayoutInflater.from(contentLayout!!.context)
        }

        return inflater!!.inflate(id, null)
    }

    fun showSuccess() {
        statusViewHelper?.restoreStatusView()
    }

    private fun createLoading() {
        if (null == loadingLayout) {
            loadingLayout = inflter(loadingLayoutId)
        }

        if (!TextUtils.isEmpty(loadingText)) {
            try {
                val view: View? = loadingLayout?.findViewById(R.id.status_view_loading_text)
                if (view is TextView) {
                    view.text = loadingText
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getLoadingView(): View {
        createLoading()
        return loadingLayout!!
    }

    fun showLoading() {
        statusViewHelper?.showStatusView(getLoadingView())
    }


    private fun createEmpty() {
        if (null == emptyView) {
            emptyView = inflter(emptyLayoutId)
        }


        try {
            val view: View = emptyView!!.findViewById(emptyClickViewId) ?: return
            view.setOnClickListener { onStatusViewClickListener?.onStatusViewEmptyClick(view) }
            if (!TextUtils.isEmpty(emptyText)) {
                val textView: View? = emptyView!!.findViewById(R.id.status_view_empty_text)
                if (textView!! is TextView) {
                    (textView as TextView).text = emptyText
                }
            }

            if (emptyImgId > 0) {
                val imageView: View? = emptyView!!.findViewById(R.id.status_view_empty_image)

                if (imageView is ImageView) {
                    imageView.setImageResource(emptyImgId)
                }
            }


            val emptyClickView: View = emptyView!!.findViewById(R.id.status_view_empty_click_view) ?: return

            if (isEmptyClickViewVisible!!) {
                emptyClickView.visibility = View.VISIBLE
                if (!TextUtils.isEmpty(emptyClickViewText)) {
                    if (emptyClickView is TextView) {
                        emptyClickView.text = emptyClickViewText
                        if (errorClickViewTextColor > 0) {
                            emptyClickView.setTextColor(emptyClickViewTextColor!!)
                        }
                    }
                }
            } else {
                emptyClickView.visibility = View.GONE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getEmptyView(): View {
        createEmpty()
        return emptyView!!
    }

    fun showEmpty() {
        statusViewHelper?.showStatusView(getEmptyView())
    }

    private fun createError() {
        if (null == errorLayout) {
            errorLayout = inflter(errorLayoutId)
        }


        try {
            val errorClickView: View? = errorLayout?.findViewById(errorClickViewId) ?: return
            errorClickView?.setOnClickListener { onStatusViewClickListener?.onStatusViewErrorClick(errorClickView) }
            if (errorImgId > 0) {
                val errorImage: View = errorLayout!!.findViewById(R.id.status_view_error_image) ?: return
                if (errorImage is ImageView) {
                    errorImage.setImageResource(errorImgId)
                }
            }
            val errorTextView: View? = errorLayout?.findViewById(R.id.status_view_error_text) ?: return
            if (!TextUtils.isEmpty(errorText)) {
                if (errorTextView is TextView) {
                    errorTextView.text = errorText
                }
            }
            if (isErrorClickViewVisible!!) {
                errorClickView!!.visibility = View.VISIBLE
                if (!TextUtils.isEmpty(errorClickViewText)) {
                    if (errorClickView is TextView) {
                        errorClickView.text = errorClickViewText
                        if (errorClickViewTextColor > 0) {
                            errorClickView.setTextColor(errorClickViewTextColor)
                        }
                    }
                }
            } else errorClickView!!.visibility = View.GONE
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getErrorView(): View {
        createError()
        return errorLayout!!
    }

    fun showError() {
        statusViewHelper?.showStatusView(getErrorView())
    }


    fun showCustomer(view: View) {
        statusViewHelper?.showStatusView(view)
    }

    fun showCusomer(@LayoutRes id: Int) {
        showCustomer(inflter(id))
    }

    class StatusViewBuilder(@NonNull contentLayout: View) {

        //==================content====================//
        internal var contentLayout: View? = null


        //==================loading====================//
        internal var loadingLayout: View? = null
        @LayoutRes
        internal var loadingLayoutId: Int = 0
        internal var loadingText: String? = ""

        //==================empty=====================//
        internal var emptyView: View? = null
        @IdRes
        internal var emptyClickViewId: Int = 0
        @LayoutRes
        internal var emptyLayoutId: Int = 0
        internal var emptyText: String? = ""
        internal var emptyClickViewText: String? = ""
        internal var emptyClickViewTextColor: Int? = 0
        internal var isEmptyClickViewVisible: Boolean? = false
        @DrawableRes
        internal var emptyImgId: Int = 0

        //==================error=====================//
        internal var errorLayout: View? = null
        @IdRes
        internal var errorClickViewId: Int = 0
        @LayoutRes
        internal var errorLayoutId: Int = 0
        internal var errorText: String? = ""
        internal var errorClickViewText: String? = ""
        internal var errorClickViewTextColor: Int = 0
        internal var isErrorClickViewVisible: Boolean? = false
        @DrawableRes
        internal var errorImgId: Int = 0

        internal var onStatusViewClickListener: StatusViewClickListener? = null


        init {
            this.contentLayout = contentLayout
            //set default views
            this.loadingLayoutId = R.layout.layout_status_view_loading
            this.emptyLayoutId = R.layout.layout_status_view_empty
            this.errorLayoutId = R.layout.layout_status_view_error

            //set default ids
            this.emptyClickViewId = R.id.status_view_empty_click_view
            this.errorClickViewId = R.id.status_view_error_click_view
            this.emptyImgId = R.mipmap.ic_launcher
            this.errorImgId = R.mipmap.ic_launcher

            this.isEmptyClickViewVisible = false
            this.isErrorClickViewVisible = false

            this.errorClickViewTextColor = ContextCompat.getColor(contentLayout.context, android.R.color.primary_text_dark)
            this.emptyClickViewTextColor = ContextCompat.getColor(contentLayout.context, android.R.color.primary_text_dark)
        }


        fun setLoadingLayout(@LayoutRes loadingLayoutId: Int): StatusViewBuilder {
            this.loadingLayoutId = loadingLayoutId
            return this
        }

        fun setLoadingView(loadingView: View?): StatusViewBuilder {
            this.loadingLayout = loadingView
            return this
        }

        fun setLoadingText(text: String?): StatusViewBuilder {
            this.loadingText = text
            return this
        }

        fun setEmptyView(view: View): StatusViewBuilder {
            this.emptyView = view
            return this
        }

        fun setEmptyLayout(@LayoutRes layoutId: Int): StatusViewBuilder {
            this.emptyLayoutId = layoutId
            return this
        }

        fun setEmptyClickId(@IdRes id: Int): StatusViewBuilder {
            this.emptyClickViewId = id
            return this
        }

        fun setEmptyText(text: String?): StatusViewBuilder {
            this.emptyText = text
            return this
        }

        fun setEmptyClickText(text: String?): StatusViewBuilder {
            this.emptyClickViewText = text
            return this
        }

        fun setEmptyImage(@DrawableRes id: Int): StatusViewBuilder {
            this.emptyImgId = id
            return this
        }

        fun setErrorLayout(@LayoutRes layoutId: Int): StatusViewBuilder {
            this.errorLayoutId = layoutId
            return this
        }

        fun setErrorView(view: View): StatusViewBuilder {
            this.errorLayout = view
            return this
        }

        fun setErrorClickId(@IdRes id: Int): StatusViewBuilder {
            this.errorClickViewId = id
            return this
        }

        fun setErrorText(text: String?): StatusViewBuilder {
            this.errorText = text
            return this
        }

        fun setErrorClickText(text: String?): StatusViewBuilder {
            this.errorClickViewText = text
            return this
        }

        fun setErrorImageId(@DrawableRes id: Int): StatusViewBuilder {
            this.errorImgId = id
            return this
        }

        fun setErrorClickViewTextColor(color: Int): StatusViewBuilder {
            this.errorClickViewTextColor = color
            return this
        }

        fun setEmptyClickViewTextColor(color: Int): StatusViewBuilder {
            this.emptyClickViewTextColor = color
            return this
        }

        fun isEmptyClickViewVisible(visible: Boolean): StatusViewBuilder {
            this.isEmptyClickViewVisible = visible
            return this
        }

        fun isErrorClickViewVisible(visible: Boolean): StatusViewBuilder {
            this.isErrorClickViewVisible = visible
            return this
        }

        fun setOnStatusViewClickListener(listener: StatusViewClickListener): StatusViewBuilder {
            this.onStatusViewClickListener = listener
            return this
        }

        @NonNull
        fun build(): StatusView {
            return StatusView(this)
        }

    }
}