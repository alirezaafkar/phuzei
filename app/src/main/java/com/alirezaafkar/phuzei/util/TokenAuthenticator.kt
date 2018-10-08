package com.alirezaafkar.phuzei.util

import com.alirezaafkar.phuzei.AUTHORIZATION
import com.alirezaafkar.phuzei.data.repository.TokenRepository
import com.alirezaafkar.phuzei.injection.component.DaggerMainComponent
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

/**
 * Created by Alireza Afkar on 10/8/18.
 */
class TokenAuthenticator : Authenticator {
    private lateinit var repository: TokenRepository

    override fun authenticate(route: Route, response: Response): Request? {
        DaggerMainComponent.builder().build()?.inject(this) ?: return null

        val accessToken = repository.refresh() ?: return null

        return response.request().newBuilder()
            .header(AUTHORIZATION, accessToken)
            .build()
    }
}
