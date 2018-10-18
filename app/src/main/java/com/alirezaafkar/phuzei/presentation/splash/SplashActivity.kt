package com.alirezaafkar.phuzei.presentation.splash

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.alirezaafkar.phuzei.App
import com.alirezaafkar.phuzei.data.pref.AppPreferences
import com.alirezaafkar.phuzei.presentation.album.AlbumActivity
import com.alirezaafkar.phuzei.presentation.main.LoginActivity
import javax.inject.Inject

/**
 * Created by Alireza Afkar on 16/9/2018AD.
 */
class SplashActivity : AppCompatActivity() {
    @Inject lateinit var prefs: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.get(this).component?.inject(this)

        if (prefs.accessToken.isNullOrEmpty()) {
            LoginActivity.start(this)
        } else {
            AlbumActivity.start(this)
        }
        finish()
    }
}
