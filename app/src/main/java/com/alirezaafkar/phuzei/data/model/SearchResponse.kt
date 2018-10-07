package com.alirezaafkar.phuzei.data.model

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("mediaItems") val mediaItems: List<Photo>,
    @SerializedName("nextPageToken") val nextPageToken: String
)
