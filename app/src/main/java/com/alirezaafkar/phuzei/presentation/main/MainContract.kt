package com.alirezaafkar.phuzei.presentation.main

import com.alirezaafkar.phuzei.presentation.base.BasePresenter
import com.alirezaafkar.phuzei.presentation.base.BaseView

/**
 * Created by Alireza Afkar on 16/9/2018AD.
 */
interface MainContract {
    interface View : BaseView {
        fun loggedOut()
    }

    interface Presenter : BasePresenter<View> {
        fun setShuffleOrder(shuffle: Boolean)
        fun isShuffleOrder(): Boolean
        fun logout()
    }
}
