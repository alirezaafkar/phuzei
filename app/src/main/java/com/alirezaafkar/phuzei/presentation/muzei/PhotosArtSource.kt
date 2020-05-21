package com.alirezaafkar.phuzei.presentation.muzei

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import androidx.core.net.toUri
import com.alirezaafkar.phuzei.App
import com.alirezaafkar.phuzei.data.model.Media
import com.alirezaafkar.phuzei.data.model.isImage
import com.alirezaafkar.phuzei.data.model.largeUrl
import com.alirezaafkar.phuzei.data.pref.AppPreferences
import com.alirezaafkar.phuzei.data.repository.PhotosRepository
import com.google.android.apps.muzei.api.Artwork
import com.google.android.apps.muzei.api.RemoteMuzeiArtSource
import java.util.*
import javax.inject.Inject

/**
 * Created by Alireza Afkar on 16/9/2018AD.
 */
class PhotosArtSource : RemoteMuzeiArtSource(SOURCE_NAME) {
    @Inject
    lateinit var repository: PhotosRepository

    @Inject
    lateinit var prefs: AppPreferences

    override fun onCreate() {
        super.onCreate()
        App.get(this).component?.inject(this)
    }

    @SuppressLint("CheckResult")
    override fun onTryUpdate(reason: Int) {
        repository.getAlbumPhotos(prefs.album ?: return)
                .subscribe(
                        {
                            prefs.pageToken = it.nextPageToken
                            onPhotosResult(it.mediaItems.filter(Media::isImage))
                        },
                        {
                            prefs.pageToken = null
                            Log.e(TAG, it.message, it)
                        }
                )
    }

    private fun onPhotosResult(medias: List<Media>) {
        if (medias.isEmpty()) {
            Log.w(TAG, "No photos returned from API")
            scheduleUpdate()
            return
        }

        val currentToken = currentArtwork?.token
        val random = Random()
        var token: String
        var media: Media

        while (true) {
            media = medias[random.nextInt(medias.size)]
            token = media.id
            if (medias.size <= 1 || token != currentToken) {
                break
            }
        }

        publish(media)

        scheduleUpdate()
    }

    private fun publish(photo: Media) {
        publishArtwork(
                Artwork.Builder()
                        .token(photo.id)
                        .title(photo.filename)
                        .byline(photo.description)
                        .viewIntent(Intent(Intent.ACTION_VIEW, photo.productUrl.toUri()))
                        .imageUri(photo.largeUrl().toUri())
                        .build()
        )
    }

    private fun scheduleUpdate() {
        scheduleUpdate(System.currentTimeMillis() + ROTATE_TIME_MILLIS)
    }

    companion object {
        private const val SOURCE_NAME = "PhotosArtSource"
        private val TAG: String = PhotosArtSource::class.java.name
        private const val ROTATE_TIME_MILLIS = 3 * 60 * 60 * 1000 // rotate every 3 hours
    }
}
