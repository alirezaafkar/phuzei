package com.alirezaafkar.phuzei.presentation.album

import com.alirezaafkar.phuzei.data.model.Album
import com.alirezaafkar.phuzei.presentation.base.BasePresenter
import com.alirezaafkar.phuzei.presentation.base.BaseView

/**
 * Created by Alireza Afkar on 6/12/2018AD.
 */
interface AlbumContract {
    interface View : BaseView {
        fun showLoading()
        fun hideLoading()
        fun onAlbums(albums: List<Album>)
        fun onError(error: String)
        fun onAlbumSelected(title: String)
        fun loadAlbums()
    }

    interface Presenter : BasePresenter<View> {
        fun getAlbums()
        fun loadMore()
        fun refresh()
        fun setAlbumType(type:Int)
        fun selectAlbum(album: Album)
        fun currentAlbum(): String?
    }
}
