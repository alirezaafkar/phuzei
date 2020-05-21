package com.alirezaafkar.phuzei.data.repository

import com.alirezaafkar.phuzei.data.api.PhotosApi
import com.alirezaafkar.phuzei.data.model.ContentFilter
import com.alirezaafkar.phuzei.data.model.FAVORITE
import com.alirezaafkar.phuzei.data.model.FeatureFilter
import com.alirezaafkar.phuzei.data.model.Filters
import com.alirezaafkar.phuzei.data.model.Search
import com.alirezaafkar.phuzei.data.model.SearchResponse
import com.alirezaafkar.phuzei.util.applyNetworkSchedulers
import io.reactivex.Single
import java.util.*
import javax.inject.Inject

/**
 * Created by Alireza Afkar on 16/9/2018AD.
 */
class PhotosRepository @Inject constructor(private var api: PhotosApi) {

    fun getAlbumPhotos(
        albumId: String?,
        category: String,
        pageToken: String? = null
    ): Single<SearchResponse> {
        val search = createSearchBody(albumId, category, pageToken)

        return if (search == null) {
            getPhotos()
        } else {
            api.getAlbumPhotos(search).compose(applyNetworkSchedulers())
        }
    }

    fun getAlbumPhotosSync(
        albumId: String?,
        category: String,
        pageToken: String? = null
    ): SearchResponse? {
        val search = createSearchBody(albumId, category, pageToken)

        return if (search == null) {
            getPhotosAsync()
        } else {
            return api.getAlbumPhotosCall(search).execute().body()
        }
    }

    fun getPhotos(): Single<SearchResponse> {
        return api.getPhotos().compose(applyNetworkSchedulers())
    }

    fun getPhotosAsync(): SearchResponse? {
        return api.getPhotosCall().execute().body()
    }

    private fun createSearchBody(
        albumId: String?,
        category: String,
        pageToken: String? = null
    ): Search? {
        var search = Search(pageToken = pageToken)

        search = if (albumId.isNullOrEmpty() && category.isEmpty()) {
            return null
        } else if (albumId.isNullOrEmpty()) {
            val filter = if (category.equals(FAVORITE, true)) {
                Filters(featureFilter = FeatureFilter(listOf(FAVORITE)))
            } else {
                Filters(contentFilter = ContentFilter(listOf(category.toUpperCase(Locale.US))))
            }
            search.copy(filters = filter)
        } else {
            search.copy(albumId = albumId)
        }
        return search
    }
}
