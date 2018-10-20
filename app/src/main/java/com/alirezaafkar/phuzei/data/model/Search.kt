package com.alirezaafkar.phuzei.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Alireza Afkar on 14/9/2018AD.
 */
data class Search(
    @SerializedName("albumId") val albumId: String,
    @SerializedName("pageSize") val pageSize: Int = 20,
    @SerializedName("pageToken") val pageToken: String? = null
)
