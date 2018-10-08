package com.alirezaafkar.phuzei.data.repository

import com.alirezaafkar.phuzei.data.api.AlbumsApi
import com.alirezaafkar.phuzei.data.model.Album
import com.alirezaafkar.phuzei.data.model.AlbumsResponse
import com.alirezaafkar.phuzei.data.pref.AppPreferences
import com.alirezaafkar.phuzei.util.applyNetworkSchedulers
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by Alireza Afkar on 16/9/2018AD.
 */
class AlbumsRepository @Inject constructor(
    private var api: AlbumsApi,
    private var prefs: AppPreferences
) {
    fun getAlbums(pageToken: String? = null): Single<AlbumsResponse> {
        return api.getAlbums("${prefs.tokenType} ${prefs.accessToken}", pageToken)
            .compose(applyNetworkSchedulers())
    }

    fun getAlbum(albumId: String): Single<Album> {
        return api.getAlbum(albumId).compose(applyNetworkSchedulers())
    }
}
