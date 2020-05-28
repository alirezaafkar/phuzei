package com.alirezaafkar.phuzei.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Alireza Afkar on 14/9/2018AD.
 */
data class Search(
    @SerializedName("pageSize") val pageSize: Int,
    @SerializedName("albumId") val albumId: String? = null,
    @SerializedName("filters") val filters: Filters? = null,
    @SerializedName("pageToken") val pageToken: String? = null
)
