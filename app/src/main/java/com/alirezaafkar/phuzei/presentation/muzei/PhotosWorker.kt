package com.alirezaafkar.phuzei.presentation.muzei

import android.content.Context
import androidx.core.net.toUri
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.alirezaafkar.phuzei.App
import com.alirezaafkar.phuzei.BuildConfig
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
        val response = try {
            repository.getAlbumPhotosSync(prefs.album, prefs.category, prefs.pageToken)
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
        val provider = ProviderContract.getProviderClient(
            applicationContext,
            BuildConfig.PHUZEI_AUTHORITY
        )
        medias.asSequence()
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
            .toList().forEach {
                provider.addArtwork(it)
            }
    }

    companion object {
        internal fun enqueueLoad(context: Context) {
            val workManager = WorkManager.getInstance(context)
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
