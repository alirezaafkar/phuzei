package com.alirezaafkar.phuzei.injection.module

import android.content.Context
import com.alirezaafkar.phuzei.data.api.AlbumsApi
import com.alirezaafkar.phuzei.data.api.PhotosApi
import com.alirezaafkar.phuzei.data.api.TokenApi
import com.alirezaafkar.phuzei.data.pref.AppPreferences
import com.alirezaafkar.phuzei.data.repository.AlbumsRepository
import com.alirezaafkar.phuzei.data.repository.PhotosRepository
import com.alirezaafkar.phuzei.data.repository.TokenRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Alireza Afkar on 17/3/2018AD.
 */
@Module(includes = [ContextModule::class])
class DataModule {
    @Provides
    @Singleton
    fun provideAppPreferences(context: Context): AppPreferences {
        return AppPreferences(context)
    }

    @Provides
    @Singleton
    fun provideTokenRepository(api: TokenApi, prefs: AppPreferences): TokenRepository {
        return TokenRepository(api, prefs)
    }

    @Provides
    @Singleton
    fun provideAlbumsRepository(api: AlbumsApi): AlbumsRepository {
        return AlbumsRepository(api)
    }

    @Provides
    @Singleton
    fun providePhotosRepository(api: PhotosApi): PhotosRepository {
        return PhotosRepository(api)
    }
}
