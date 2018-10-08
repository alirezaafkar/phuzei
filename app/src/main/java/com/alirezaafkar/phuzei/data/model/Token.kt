package com.alirezaafkar.phuzei.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Alireza Afkar on 14/9/2018AD.
 */
data class Token(
    @SerializedName("scope") val scope: String,
    @SerializedName("expires_in") val expiresIn: Int,
    @SerializedName("token_type") val tokenType: String,
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String?
)
