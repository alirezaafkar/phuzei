package com.alirezaafkar.phuzei.data.repository

import com.alirezaafkar.phuzei.data.api.PhotosApi
import com.alirezaafkar.phuzei.util.applyNetworkSchedulers
import com.alirezaafkar.phuzei.data.model.Photo
import com.alirezaafkar.phuzei.data.model.Search
import com.alirezaafkar.phuzei.data.model.SearchResponse
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by Alireza Afkar on 16/9/2018AD.
 */
class PhotosRepository @Inject constructor(private var api: PhotosApi) {
    fun getAlbumPhotos(albumId: String): Single<SearchResponse> {
        val search = Search(albumId)
        return api.getAlbumPhotos(search).compose(applyNetworkSchedulers())
    }

    fun getAlbumPhotosSync(albumId: String): List<Photo>? {
        val search = Search(albumId)
        return api.getAlbumPhotosCall(search).execute().body()?.mediaItems
    }

    fun getPhotos(): Single<SearchResponse> {
        return api.getPhotos().compose(applyNetworkSchedulers())
    }
}
