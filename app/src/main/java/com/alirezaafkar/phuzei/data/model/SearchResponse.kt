package com.alirezaafkar.phuzei.data.model

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("mediaItems") val mediaItems: List<Media>,
    @SerializedName("nextPageToken") val nextPageToken: String
)
