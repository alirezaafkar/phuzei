package com.alirezaafkar.phuzei.data.api

import com.alirezaafkar.phuzei.CLIENT_ID
import com.alirezaafkar.phuzei.GRANT_TYPE
import com.alirezaafkar.phuzei.REDIRECT_URI
import com.alirezaafkar.phuzei.data.model.Token
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


/**
 * Created by Alireza Afkar on 14/9/2018AD.
 */
interface TokenApi {
    @FormUrlEncoded
    @POST("https://www.googleapis.com/oauth2/v4/token")
    fun request(
        @Field("code") code: String,
        @Field("client_id") client_id: String = CLIENT_ID,
        @Field("redirect_uri") redirect_uri: String = REDIRECT_URI,
        @Field("grant_type") grant_type: String = GRANT_TYPE
    ): Single<Token>

    @FormUrlEncoded
    @POST("https://www.googleapis.com/oauth2/v4/token")
    fun refresh(
        @Field("refresh_token") refresh_token: String?,
        @Field("client_id") client_id: String = CLIENT_ID,
        @Field("grant_type") grant_type: String = GRANT_TYPE
    ): Single<Token>
}
