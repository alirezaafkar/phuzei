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
            prefs.refreshToken = it.refreshToken
        }.compose(applyNetworkSchedulers())
    }

    fun refresh(): Single<Unit> {
        return api.refresh(prefs.refreshToken).map {
            prefs.tokenType = it.tokenType
            prefs.accessToken = it.accessToken
            prefs.refreshToken = it.refreshToken
        }.compose(applyNetworkSchedulers())
    }
}
