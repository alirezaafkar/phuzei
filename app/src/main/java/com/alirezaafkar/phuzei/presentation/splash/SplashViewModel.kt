package com.alirezaafkar.phuzei.presentation.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.alirezaafkar.phuzei.data.pref.AppPreferences
import com.alirezaafkar.phuzei.util.SingleLiveEvent
import javax.inject.Inject

/**
 * Created by Alireza Afkar on 16/9/2018AD.
 */
class SplashViewModel @Inject constructor(
    private val prefs: AppPreferences
) : ViewModel() {

    private val _mainActivityObservable = SingleLiveEvent<Unit>()
    val mainActivityObservable: LiveData<Unit> = _mainActivityObservable

    private val _loginActivityObservable = SingleLiveEvent<Unit>()
    val loginActivityObservable: LiveData<Unit> = _loginActivityObservable

    fun subscribe() {
        if (prefs.accessToken.isNullOrEmpty()) {
            _loginActivityObservable.value = null
        } else {
            _mainActivityObservable.value = null
        }
    }
}
