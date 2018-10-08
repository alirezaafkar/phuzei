package com.alirezaafkar.phuzei.presentation.album

import com.alirezaafkar.phuzei.data.pref.AppPreferences
import com.alirezaafkar.phuzei.data.repository.AlbumsRepository
import com.alirezaafkar.phuzei.data.repository.TokenRepository
import com.alirezaafkar.phuzei.presentation.muzei.PhotosWorker
import io.reactivex.disposables.CompositeDisposable
import retrofit2.HttpException
import javax.inject.Inject

/**
 * Created by Alireza Afkar on 16/9/2018AD.
 */
class AlbumPresenter(override val view: AlbumContract.View) : AlbumContract.Presenter {
    override var disposables: CompositeDisposable? = CompositeDisposable()

    private var pageToken: String? = null

    @Inject lateinit var tokenRepository: TokenRepository
    @Inject lateinit var repository: AlbumsRepository
    @Inject lateinit var prefs: AppPreferences

    override fun onCreate() {
        super.onCreate()
        view.mainComponent().inject(this)
    }

    override fun selectAlbum(albumId: String) {
        prefs.album = albumId
        PhotosWorker.enqueueLoad()
        view.finish()
    }

    override fun currentAlbum(): String? = prefs.album

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
                        onAlbumsError(it)
                    }
                )
        )
    }

    private fun onAlbumsError(throwable: Throwable) {
        if (throwable is HttpException && throwable.code() == 401) {
            refreshToken()
        } else {
            view.onError(throwable.localizedMessage)
        }
    }

    private fun refreshToken() {
        disposables?.add(
            tokenRepository.refresh()
                .doOnSubscribe { view.showLoading() }
                .doAfterTerminate { view.hideLoading() }
                .subscribe(
                    {
                        getAlbums()
                    }, {
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
}
