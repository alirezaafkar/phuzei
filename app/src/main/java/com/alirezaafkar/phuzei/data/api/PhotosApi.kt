package com.alirezaafkar.phuzei.data.api

import com.alirezaafkar.phuzei.data.model.Search
import com.alirezaafkar.phuzei.data.model.SearchResponse
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Created by Alireza Afkar on 14/9/2018AD.
 */
interface PhotosApi {

    @POST("mediaItems:search")
    fun getAlbumPhotos(@Body search: Search): Single<SearchResponse>

    @POST("./mediaItems:search")
    fun getAlbumPhotosCall(@Body search: Search): Call<SearchResponse>

    @GET("mediaItems")
    fun getPhotos(
        @Query("pageSize") pageSize: Int,
        @Query("pageToken") pageToken: String?
    ): Single<SearchResponse>

    @GET("mediaItems")
    fun getPhotosCall(
        @Query("pageSize") pageSize: Int,
        @Query("pageToken") pageToken: String?
    ): Call<SearchResponse>
}
