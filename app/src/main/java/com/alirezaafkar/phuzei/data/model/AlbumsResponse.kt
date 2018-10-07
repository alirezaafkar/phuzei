package com.alirezaafkar.phuzei.data.model

import com.google.gson.annotations.SerializedName

data class AlbumsResponse(
    @SerializedName("albums") val albums: List<Album>,
    @SerializedName("nextPageToken") val nextPageToken: String
)
