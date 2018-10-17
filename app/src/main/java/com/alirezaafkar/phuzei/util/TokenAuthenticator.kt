package com.alirezaafkar.phuzei.util

import com.alirezaafkar.phuzei.AUTHORIZATION
import com.alirezaafkar.phuzei.REFRESH_URL
import com.alirezaafkar.phuzei.contentType
import com.alirezaafkar.phuzei.data.model.Token
import com.alirezaafkar.phuzei.data.model.TokenRequest
import com.alirezaafkar.phuzei.data.pref.AppPreferences
import com.google.gson.Gson
import okhttp3.*
import javax.inject.Inject


/**
 * Created by Alireza Afkar on 10/8/18.
 */
class TokenAuthenticator @Inject constructor(
    private val gson: Gson,
    private val prefs: AppPreferences
) : Authenticator {

    override fun authenticate(route: Route, response: Response): Request? {
        val accessToken = refreshToken() ?: return null

        return response.request().newBuilder()
            .header(AUTHORIZATION, accessToken)
            .build()
    }

    private fun refreshToken(): String? {
        val token = prefs.refreshToken ?: return null

        val body = gson.toJson(TokenRequest(token))
        val request = Request.Builder()
            .url(REFRESH_URL)
            .post(RequestBody.create(contentType, body))
            .build()

        val response = OkHttpClient().newCall(request).execute()
        val responseBody = response?.body()?.string() ?: return null
        val tokenResponse = gson.fromJson(responseBody, Token::class.java) ?: return null

        with(tokenResponse) {
            prefs.tokenType = tokenType
            prefs.accessToken = accessToken
            prefs.refreshToken = refreshToken
        }
        return tokenResponse.accessToken
    }
}
