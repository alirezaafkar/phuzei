package com.alirezaafkar.phuzei.presentation.album

import com.alirezaafkar.phuzei.data.pref.AppPreferences
import com.alirezaafkar.phuzei.data.repository.AlbumsRepository
import com.alirezaafkar.phuzei.presentation.muzei.PhotosWorker
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * Created by Alireza Afkar on 16/9/2018AD.
 */
class AlbumPresenter(override val view: AlbumContract.View) : AlbumContract.Presenter {
    override var disposables: CompositeDisposable? = CompositeDisposable()

    private var pageToken: String? = null

    @Inject lateinit var prefs: AppPreferences
    @Inject lateinit var repository: AlbumsRepository

    override fun onCreate() {
        super.onCreate()
        view.mainComponent().inject(this)
        getAlbums()
    }

    override fun selectAlbum(albumId: String) {
        prefs.album = albumId
        prefs.pageToken = null
        loadAlbumImages()
    }

    override fun currentAlbum(): String? = prefs.album

    override fun setShuffleOrder(shuffle: Boolean) {
        prefs.shuffle = shuffle
        if (!prefs.album.isNullOrBlank()) {
            loadAlbumImages()
        }
    }

    override fun isShuffleOrder() = prefs.shuffle

    private fun loadAlbumImages() {
        PhotosWorker.enqueueLoad()
        view.finish()
    }

    override fun getAlbums() {
        disposables?.add(
            repository.getAlbums(pageToken)
                .doOnSubscribe { view.showLoading() }
                .doAfterTerminate { view.hideLoading() }
                .subscribe(
                    {
                        pageToken = it.nextPageToken
                        view.onAlbums(it.albums)
                    },
                    {
                        view.onError(it.localizedMessage)
                    }
                )
        )
    }

    override fun loadMore() {
        if (!pageToken.isNullOrBlank()) {
            getAlbums()
        }
    }

    override fun refresh() {
        pageToken = null
        getAlbums()
    }

    override fun logout() {
        prefs.logout()
        view.loggedOut()
    }
}
