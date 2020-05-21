package com.alirezaafkar.phuzei.injection.component

import com.alirezaafkar.phuzei.injection.module.ContextModule
import com.alirezaafkar.phuzei.injection.module.DataModule
import com.alirezaafkar.phuzei.injection.module.NetworkModule
import com.alirezaafkar.phuzei.injection.module.ViewModelModule
import com.alirezaafkar.phuzei.injection.util.ViewModelFactoryModule
import com.alirezaafkar.phuzei.presentation.album.AlbumFragment
import com.alirezaafkar.phuzei.presentation.login.LoginActivity
import com.alirezaafkar.phuzei.presentation.main.MainActivity
import com.alirezaafkar.phuzei.presentation.muzei.PhotosArtProvider
import com.alirezaafkar.phuzei.presentation.muzei.PhotosArtSource
import com.alirezaafkar.phuzei.presentation.muzei.PhotosWorker
import com.alirezaafkar.phuzei.presentation.setting.SettingsFragment
import com.alirezaafkar.phuzei.presentation.splash.SplashActivity
import com.alirezaafkar.phuzei.util.TokenAuthenticator
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Alireza Afkar on 16/3/2018AD.
 */
@Singleton
@Component(
    modules = [
        DataModule::class,
        ContextModule::class,
        NetworkModule::class,
        ViewModelModule::class,
        ViewModelFactoryModule::class
    ]
)
interface MainComponent {

    fun inject(photosWorker: PhotosWorker)
    fun inject(mainActivity: MainActivity)
    fun inject(loginActivity: LoginActivity)
    fun inject(albumFragment: AlbumFragment)
    fun inject(splashActivity: SplashActivity)
    fun inject(photosArtSource: PhotosArtSource)
    fun inject(settingsFragment: SettingsFragment)
    fun inject(photosArtProvider: PhotosArtProvider)
    fun inject(tokenAuthenticator: TokenAuthenticator)
}
