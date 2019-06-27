package com.alirezaafkar.phuzei.presentation.album

import com.alirezaafkar.phuzei.data.model.Album
import com.alirezaafkar.phuzei.data.model.BaseAlbumResponse
import com.alirezaafkar.phuzei.data.pref.AppPreferences
import com.alirezaafkar.phuzei.data.repository.AlbumsRepository
import com.alirezaafkar.phuzei.presentation.muzei.PhotosWorker
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * Created by Alireza Afkar on 6/12/2018AD.
 */
class AlbumPresenter(override val view: AlbumContract.View) : AlbumContract.Presenter {
    override var disposables: CompositeDisposable? = CompositeDisposable()

    @Inject lateinit var prefs: AppPreferences
    @Inject lateinit var repository: AlbumsRepository

    private var loading = false
    private var pageToken: String? = null
    private var albumType = AlbumFragment.TYPE_ALBUMS

    override fun onCreate() {
        super.onCreate()
        view.mainComponent().inject(this)
    }

    override fun setAlbumType(type: Int) {
        albumType = type
    }

    override fun selectAlbum(album: Album) {
        prefs.album = album.id
        prefs.pageToken = null
        loadAlbumImages(album.title)
    }

    override fun currentAlbum(): String? = prefs.album

    private fun loadAlbumImages(title: String?) {
        PhotosWorker.enqueueLoad()
        title?.let {
            view.onAlbumSelected(it)
        }
    }

    override fun getAlbums() {
        disposables?.add(
            getAlbumsApi()
                .doOnSubscribe {
                    loading = true
                    view.showLoading()
                }
                .doAfterTerminate { view.hideLoading() }
                .subscribe({
                    loading = false
                    pageToken = it.nextPageToken
                    if (it.albums != null) {
                        view.onAlbums(it.albums!!)
                    } else {
                        loadMore()
                    }
                }, {
                    loading = false
                    view.onError(it.localizedMessage)
                })
        )
    }

    private fun getAlbumsApi(): Single<out BaseAlbumResponse> {
        return if (albumType == AlbumFragment.TYPE_ALBUMS) {
            repository.getAlbums(pageToken)
        } else {
            repository.getSharedAlbums(pageToken)
        }
    }

    override fun loadMore() {
        if (!pageToken.isNullOrBlank() && !loading) {
            getAlbums()
        }
    }

    override fun refresh() {
        pageToken = null
        getAlbums()
    }
}
