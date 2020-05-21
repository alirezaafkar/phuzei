package com.alirezaafkar.phuzei.presentation.album

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alirezaafkar.phuzei.data.model.Album
import com.alirezaafkar.phuzei.data.model.BaseAlbumResponse
import com.alirezaafkar.phuzei.data.pref.AppPreferences
import com.alirezaafkar.phuzei.data.repository.AlbumsRepository
import com.alirezaafkar.phuzei.presentation.muzei.PhotosWorker
import com.alirezaafkar.phuzei.util.SingleLiveEvent
import com.alirezaafkar.phuzei.util.addTo
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * Created by Alireza Afkar on 6/12/2018AD.
 */
class AlbumViewModel @Inject constructor(
    private val prefs: AppPreferences,
    private val repository: AlbumsRepository,
    private val disposable: CompositeDisposable
) : ViewModel() {

    private val _selectAlbumObservable = SingleLiveEvent<String>()
    val selectAlbumObservable: LiveData<String> = _selectAlbumObservable

    private val _loadingObservable = MutableLiveData<Boolean>()
    val loadingObservable: LiveData<Boolean> = _loadingObservable

    private val _albumsObservable = MutableLiveData<List<Album>>()
    val albumsObservable: LiveData<List<Album>> = _albumsObservable

    private val _errorObservable = SingleLiveEvent<String>()
    val errorObservable: LiveData<String> = _errorObservable

    private var pageToken: String? = null
    val currentAlbum: String? = prefs.album
    private var albumType = AlbumFragment.TYPE_ALBUMS

    fun subscribe(albumType: Int) {
        this.albumType = albumType
        getAlbums()
    }

    fun onSelectAlbum(album: Album) {
        prefs.album = album.id
        prefs.pageToken = null
        loadAlbumImages(album.title)
    }

    fun onRefresh() {
        pageToken = null
        getAlbums()
    }

    fun onLoadMore() {
        if (!pageToken.isNullOrBlank() && loadingObservable.value != true) {
            getAlbums()
        }
    }

    private fun getAlbums() {
        getAlbumsApi()
            .doOnSubscribe { _loadingObservable.value = true }
            .doAfterTerminate { _loadingObservable.value = false }
            .doOnSuccess { pageToken = it.nextPageToken }
            .subscribe({
                if (it.albums.isNullOrEmpty()) {
                    onLoadMore()
                } else {
                    _albumsObservable.value = it.albums
                }
            }, {
                _errorObservable.value = it.localizedMessage
            })
            .addTo(disposable)
    }

    private fun loadAlbumImages(title: String?) {
        PhotosWorker.enqueueLoad()
        title?.let {
            _selectAlbumObservable.value = it
        }
    }

    private fun getAlbumsApi(): Single<out BaseAlbumResponse> {
        return if (albumType == AlbumFragment.TYPE_ALBUMS) {
            repository.getAlbums(pageToken)
        } else {
            repository.getSharedAlbums(pageToken)
        }
    }

    override fun onCleared() {
        disposable.clear()
    }
}
