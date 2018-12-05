package com.alirezaafkar.phuzei.presentation.muzei

import android.content.Context
import androidx.core.net.toUri
import androidx.work.*
import com.alirezaafkar.phuzei.App
import com.alirezaafkar.phuzei.data.model.Media
import com.alirezaafkar.phuzei.data.model.isImage
import com.alirezaafkar.phuzei.data.model.largeUrl
import com.alirezaafkar.phuzei.data.pref.AppPreferences
import com.alirezaafkar.phuzei.data.repository.PhotosRepository
import com.google.android.apps.muzei.api.provider.Artwork
import com.google.android.apps.muzei.api.provider.ProviderContract
import java.io.IOException
import javax.inject.Inject

/**
 * Created by Alireza Afkar on 16/9/2018AD.
 */
class PhotosWorker(
        context: Context,
        workerParams: WorkerParameters
) : Worker(context, workerParams) {

    @Inject
    lateinit var repository: PhotosRepository
    @Inject
    lateinit var prefs: AppPreferences

    init {
        App.get(context).component?.inject(this)
    }

    override fun doWork(): Result {
        val album = prefs.album ?: return Result.failure()

        val response = try {
            repository.getAlbumPhotosSync(album, prefs.pageToken)
        } catch (e: IOException) {
            null
        }

        response?.let {
            prefs.pageToken = it.nextPageToken
            onPhotosResult(
                    if (prefs.shuffle) {
                        it.mediaItems.shuffled()
                    } else {
                        it.mediaItems
                    }
            )
            return Result.success()
        } ?: kotlin.run {
            prefs.pageToken = null
            return Result.failure()
        }
    }

    private fun onPhotosResult(medias: List<Media>) {
        deleteAllImages()
        medias
                .asSequence()
                .filter(Media::isImage)
                .map { photo ->
                    Artwork().apply {
                        token = photo.id
                        title = photo.filename
                        byline = photo.description
                        webUri = photo.productUrl.toUri()
                        persistentUri = photo.largeUrl().toUri()

                    }
                }
                .toList().forEach { artwork ->
                    ProviderContract.Artwork.addArtwork(
                            applicationContext,
                            PhotosArtProvider::class.java,
                            artwork
                    )
                }
    }

    private fun deleteAllImages() {
        val contentUri = ProviderContract.Artwork.getContentUri(
                applicationContext, PhotosArtProvider::class.java
        )
        applicationContext.contentResolver.delete(
                contentUri, null, null
        )
    }

    companion object {
        internal fun enqueueLoad() {
            val workManager = WorkManager.getInstance()
            workManager.enqueue(
                    OneTimeWorkRequestBuilder<PhotosWorker>()
                            .setConstraints(
                                    Constraints.Builder()
                                            .setRequiredNetworkType(NetworkType.CONNECTED)
                                            .build()
                            )
                            .build()
            )
        }
    }
}
