package com.alirezaafkar.phuzei.presentation.muzei

import com.google.android.apps.muzei.api.provider.MuzeiArtProvider

/**
 * Created by Alireza Afkar on 16/9/2018AD.
 */
class PhotosArtProvider : MuzeiArtProvider() {

    override fun onLoadRequested(initial: Boolean) {
        val ctx = context ?: return
        PhotosWorker.enqueueLoad(ctx, false)
    }
}
