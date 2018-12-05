package com.alirezaafkar.phuzei.data.model

/**
 * Created by Alireza Afkar on 6/12/2018AD.
 */
abstract class BaseAlbumResponse {
    abstract var albums: List<Album>
    abstract var nextPageToken: String
}
