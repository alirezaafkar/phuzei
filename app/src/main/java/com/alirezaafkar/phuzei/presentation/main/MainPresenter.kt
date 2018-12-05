package com.alirezaafkar.phuzei.presentation.main

import com.alirezaafkar.phuzei.data.pref.AppPreferences
import com.alirezaafkar.phuzei.data.repository.AlbumsRepository
import com.alirezaafkar.phuzei.presentation.muzei.PhotosWorker
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * Created by Alireza Afkar on 16/9/2018AD.
 */
class MainPresenter(override val view: MainContract.View) : MainContract.Presenter {
    override var disposables: CompositeDisposable? = CompositeDisposable()


    @Inject lateinit var prefs: AppPreferences
    @Inject lateinit var repository: AlbumsRepository

    override fun onCreate() {
        super.onCreate()
        view.mainComponent().inject(this)
    }


    override fun setShuffleOrder(shuffle: Boolean) {
        prefs.shuffle = shuffle
        if (!prefs.album.isNullOrBlank()) {
            PhotosWorker.enqueueLoad()
        }
    }

    override fun isShuffleOrder() = prefs.shuffle

    override fun logout() {
        prefs.logout()
        view.loggedOut()
    }
}
