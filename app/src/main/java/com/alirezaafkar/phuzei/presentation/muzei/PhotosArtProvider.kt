package com.alirezaafkar.phuzei.presentation.muzei

import com.alirezaafkar.phuzei.BuildConfig
import com.google.android.apps.muzei.api.provider.MuzeiArtProvider
import com.google.android.apps.muzei.api.provider.ProviderContract

/**
 * Created by Alireza Afkar on 16/9/2018AD.
 */
class PhotosArtProvider : MuzeiArtProvider() {

    override fun onLoadRequested(initial: Boolean) {
        val ctx = context ?: return
        val provider = ProviderContract.getProviderClient(ctx, BuildConfig.APPLICATION_ID)
        ctx.contentResolver.delete(provider.contentUri, null, null)
        PhotosWorker.enqueueLoad(ctx)
    }
}
