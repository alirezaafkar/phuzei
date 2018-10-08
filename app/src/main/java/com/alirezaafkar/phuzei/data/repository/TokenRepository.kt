package com.alirezaafkar.phuzei.data.repository

import com.alirezaafkar.phuzei.data.api.TokenApi
import com.alirezaafkar.phuzei.data.pref.AppPreferences
import com.alirezaafkar.phuzei.util.applyNetworkSchedulers
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by Alireza Afkar on 16/9/2018AD.
 */
class TokenRepository @Inject constructor(
    private var api: TokenApi,
    private var prefs: AppPreferences
) {
    fun request(code: String): Single<Unit> {
        return api.request(code).map {
            prefs.tokenType = it.tokenType
            prefs.accessToken = it.accessToken
            it.refreshToken?.let { token ->
                prefs.refreshToken = token
            }
        }.compose(applyNetworkSchedulers())
    }

    fun refresh(): String? {
        val token = api.refresh(prefs.refreshToken).execute().body() ?: return null

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
