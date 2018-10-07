package com.alirezaafkar.phuzei.presentation.base

import io.reactivex.disposables.CompositeDisposable

/**
 * Created by Alireza Afkar on 16/3/2018AD.
 */
interface BasePresenter<out V : BaseView> {
    val view: V
    var disposables: CompositeDisposable?

    fun onCreate() {

    }

    fun onDestroy() {
        disposables?.dispose()
    }
}
