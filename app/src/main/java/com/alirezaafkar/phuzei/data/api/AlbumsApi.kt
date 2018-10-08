package com.alirezaafkar.phuzei.data.api

import com.alirezaafkar.phuzei.AUTHORIZATION
import com.alirezaafkar.phuzei.data.model.Album
import com.alirezaafkar.phuzei.data.model.AlbumsResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Alireza Afkar on 14/9/2018AD.
 */
interface AlbumsApi {
    @GET("albums")
    fun getAlbums(@Header(AUTHORIZATION) token: String?, @Query("pageToken") pageToken: String? = null): Single<AlbumsResponse>

    @GET("albums/{albumId}")
    fun getAlbum(@Path("albumId") id: String): Single<Album>
}
