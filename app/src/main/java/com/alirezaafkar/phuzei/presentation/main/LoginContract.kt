package com.alirezaafkar.phuzei.presentation.main

import com.alirezaafkar.phuzei.presentation.base.BasePresenter
import com.alirezaafkar.phuzei.presentation.base.BaseView

/**
 * Created by Alireza Afkar on 14/9/2018AD.
 */
interface LoginContract {
    interface View : BaseView {
        fun showLoading()
        fun hideLoading()
        fun onError(error: String)
        fun onTokenResult()
        fun openBrowser()
    }

    interface Presenter : BasePresenter<View> {
        fun signIn()
        fun getToken(code: String)
    }
}
