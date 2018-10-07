package com.alirezaafkar.phuzei.data.api

import com.alirezaafkar.phuzei.data.model.Search
import com.alirezaafkar.phuzei.data.model.SearchResponse
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by Alireza Afkar on 14/9/2018AD.
 */
interface PhotosApi {
    @POST("mediaItems:search")
    fun getAlbumPhotos(@Body search: Search): Single<SearchResponse>

    @POST("./mediaItems:search")
    fun getAlbumPhotosCall(@Body search: Search): Call<SearchResponse>

    @POST("mediaItems")
    fun getPhotos(): Single<SearchResponse>
}
