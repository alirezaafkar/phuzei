package com.alirezaafkar.phuzei.data.model

import com.google.gson.annotations.SerializedName

data class Metadata(
    @SerializedName("width") val width: String,
    @SerializedName("height") val height: String
)
