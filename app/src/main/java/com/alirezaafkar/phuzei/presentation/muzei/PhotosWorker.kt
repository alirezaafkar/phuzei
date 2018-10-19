package com.alirezaafkar.phuzei.presentation.muzei

import android.content.Context
import androidx.core.net.toUri
import androidx.work.*
import com.alirezaafkar.phuzei.App
import com.alirezaafkar.phuzei.data.model.Photo
import com.alirezaafkar.phuzei.data.model.height
import com.alirezaafkar.phuzei.data.model.width
import com.alirezaafkar.phuzei.data.pref.AppPreferences
import com.alirezaafkar.phuzei.data.repository.PhotosRepository
import com.google.android.apps.muzei.api.provider.Artwork
import com.google.android.apps.muzei.api.provider.ProviderContract
import javax.inject.Inject

/**
 * Created by Alireza Afkar on 16/9/2018AD.
 */
class PhotosWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    @Inject lateinit var repository: PhotosRepository
    @Inject lateinit var prefs: AppPreferences

    init {
        App.get(context).component?.inject(this)
    }

    override fun doWork(): Result {
        val album = prefs.album ?: return Result.FAILURE
        repository.getAlbumPhotosSync(album)?.let {
            onPhotosResult(if (prefs.shuffle) it.shuffled() else it)
            return Result.SUCCESS
        } ?: kotlin.run {
            return Result.FAILURE
        }
    }

    private fun onPhotosResult(photos: List<Photo>) {
        deleteAllImages()
        photos.map { photo ->
            Artwork().apply {
                token = photo.id
                title = photo.filename
                attribution = photo.description
                webUri = photo.productUrl.toUri()
                persistentUri = "${photo.baseUrl}=w${photo.width()}-h${photo.height()}".toUri()

            }
        }.forEach { artwork ->
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
