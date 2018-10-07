package com.alirezaafkar.phuzei.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Alireza Afkar on 14/9/2018AD.
 */
data class Album(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("mediaItemsCount") val itemsCount: String,
    @SerializedName("coverPhotoBaseUrl") val coverPhotoUrl: String
)
