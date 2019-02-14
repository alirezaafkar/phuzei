package com.alirezaafkar.phuzei.data.model

import com.google.gson.annotations.SerializedName

data class AlbumsResponse(
    @SerializedName("albums") override var albums: List<Album>?,
    @SerializedName("nextPageToken") override var nextPageToken: String
) : BaseAlbumResponse()
