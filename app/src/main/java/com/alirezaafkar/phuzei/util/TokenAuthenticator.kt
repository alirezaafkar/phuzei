package com.alirezaafkar.phuzei.util

import com.alirezaafkar.phuzei.AUTHORIZATION
import com.alirezaafkar.phuzei.CLIENT_ID
import com.alirezaafkar.phuzei.REFRESH_GRANT_TYPE
import com.alirezaafkar.phuzei.REFRESH_URL
import com.alirezaafkar.phuzei.data.model.Token
import com.alirezaafkar.phuzei.data.pref.AppPreferences
import com.google.gson.Gson
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject


/**
 * Created by Alireza Afkar on 10/8/18.
 */
class TokenAuthenticator @Inject constructor(private val prefs: AppPreferences) : Authenticator {

    override fun authenticate(route: Route, response: Response): Request? {
        val accessToken = refreshToken() ?: return null

        return response.request().newBuilder()
            .header(AUTHORIZATION, accessToken)
            .build()
    }

    private fun refreshToken(): String? {
        val refreshUrl = URL(REFRESH_URL)
        val urlConnection = (refreshUrl.openConnection() as HttpURLConnection).apply {
            setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            requestMethod = "POST"
            useCaches = false
            doOutput = true
            doInput = true
        }
        val body =
            "grant_type=$REFRESH_GRANT_TYPE&client_id=$CLIENT_ID&refresh_token=${prefs.refreshToken}"

        DataOutputStream(urlConnection.outputStream).apply {
            writeBytes(body)
            flush()
            close()
        }

        if (urlConnection.responseCode != 200) return null

        val response = StringBuffer()
        with(BufferedReader(InputStreamReader(urlConnection.inputStream))) {
            readLines().forEach {
                response.append(it)
            }
            close()
        }

        val token = Gson().fromJson<Token>(response.toString(), Token::class.java)
        with(token) {
            prefs.tokenType = tokenType
            prefs.accessToken = accessToken
            refreshToken?.let {
                prefs.refreshToken = it
            }
        }
        return token.accessToken
    }

}
