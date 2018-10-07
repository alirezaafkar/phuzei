package com.alirezaafkar.phuzei.util

import com.alirezaafkar.phuzei.AUTHORIZATION
import com.alirezaafkar.phuzei.data.pref.AppPreferences
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.io.IOException
import javax.inject.Inject

/**
 * Created by Alireza Afkar on 17/9/2018AD.
 */
class TokenAuthenticator @Inject constructor(val prefs: AppPreferences) : Authenticator {
    @Throws(IOException::class)
    override fun authenticate(route: Route, response: Response): Request? {
        if (response.code() != 401) return null
        val refreshToken = prefs.refreshToken ?: return null

        /*val tokenResponse = api.refresh(refreshToken).execute()
        if (!tokenResponse.isSuccessful) return null
        val token = tokenResponse.body() ?: return null

        prefs.tokenType = token.tokenType
        prefs.accessToken = token.accessToken
        prefs.refreshToken = token.refreshToken
*/
        return response.request().newBuilder()
            .header(AUTHORIZATION, refreshToken)
            .build()
    }


}
