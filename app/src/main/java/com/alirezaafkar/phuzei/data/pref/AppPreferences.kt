package com.alirezaafkar.phuzei.data.pref

import android.content.Context
import android.preference.PreferenceManager
import androidx.core.content.edit

/**
 * Created by Alireza Afkar on 16/9/2018AD.
 */
class AppPreferences(context: Context) {
    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    var accessToken: String?
        get() = preferences.getString(ACCESS_TOKEN, "")
        set(value) = preferences.edit { putString(ACCESS_TOKEN, value) }

    var refreshToken: String?
        get() = preferences.getString(REFRESH_TOKEN, "")
        set(value) = preferences.edit { putString(REFRESH_TOKEN, value) }

    var tokenType: String?
        get() = preferences.getString(TOKEN_TYPE, "")
        set(value) = preferences.edit { putString(TOKEN_TYPE, value) }

    var album: String?
        get() = preferences.getString(ALBUM, "")
        set(value) = preferences.edit { putString(ALBUM, value) }

    var shuffle: Boolean
        get() = preferences.getBoolean(SHUFFLE, false)
        set(value) = preferences.edit { putBoolean(SHUFFLE, value) }

    fun logout() {
        preferences.edit {
            putString(ALBUM, null)
            putBoolean(SHUFFLE, false)
            putString(TOKEN_TYPE, null)
            putString(ACCESS_TOKEN, null)
            putString(REFRESH_TOKEN, null)
        }
    }

    companion object {
        private const val ALBUM = "album"
        private const val SHUFFLE = "shuffle"
        private const val TOKEN_TYPE = "token_type"
        private const val ACCESS_TOKEN = "access_token"
        private const val REFRESH_TOKEN = "refresh_token"
    }
}

fun AppPreferences.token(token: String? = accessToken) =
    if (token.isNullOrBlank()) "" else "$tokenType $token"
