package com.alirezaafkar.phuzei.data.model

import com.alirezaafkar.phuzei.CLIENT_ID
import com.alirezaafkar.phuzei.REFRESH_GRANT_TYPE

/**
 * Created by Alireza Afkar on 10/16/18.
 */
data class TokenRequest(
    val refresh_token: String,
    val client_id: String = CLIENT_ID,
    val grant_type: String = REFRESH_GRANT_TYPE
)
