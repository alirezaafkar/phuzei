package com.alirezaafkar.phuzei.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.alirezaafkar.phuzei.data.pref.AppPreferences
import com.alirezaafkar.phuzei.presentation.muzei.PhotosWorker
import com.alirezaafkar.phuzei.util.SingleLiveEvent
import javax.inject.Inject

/**
 * Created by Alireza Afkar on 16/9/2018AD.
 */
class MainViewModel @Inject constructor(
    private val prefs: AppPreferences
) : ViewModel() {

    private val _logoutObservable = SingleLiveEvent<Unit>()
    val logoutObservable: LiveData<Unit> = _logoutObservable

    val isShuffleOrder = prefs.shuffle

    fun onShuffleOrder(shuffle: Boolean) {
        prefs.shuffle = shuffle
        if (!prefs.album.isNullOrBlank()) {
            PhotosWorker.enqueueLoad()
        }
    }

    fun onLogout() {
        prefs.logout()
        _logoutObservable.value = null
    }
}
