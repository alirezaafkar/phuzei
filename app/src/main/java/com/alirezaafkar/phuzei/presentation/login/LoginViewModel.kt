package com.alirezaafkar.phuzei.presentation.login

import android.content.Intent
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alirezaafkar.phuzei.data.repository.TokenRepository
import com.alirezaafkar.phuzei.util.SingleLiveEvent
import com.alirezaafkar.phuzei.util.addTo
import io.reactivex.disposables.CompositeDisposable
import okhttp3.HttpUrl
import javax.inject.Inject

/**
 * Created by Alireza Afkar on 14/9/2018AD.
 */
class LoginViewModel @Inject constructor(
    private val authorizeUrl: HttpUrl,
    private val disposable: CompositeDisposable,
    private val tokenRepository: TokenRepository
) : ViewModel() {

    private val _loadingObservable = MutableLiveData<Boolean>()
    val loadingObservable: LiveData<Boolean> = _loadingObservable

    private val _authorizeObservable = SingleLiveEvent<Intent>()
    val authorizeObservable: LiveData<Intent> = _authorizeObservable

    private val _resultObservable = SingleLiveEvent<Unit>()
    val resultObservable: LiveData<Unit> = _resultObservable

    private val _errorObservable = SingleLiveEvent<String>()
    val errorObservable: LiveData<String> = _errorObservable

    fun onSignIn() {
        _authorizeObservable.value = Intent(
            Intent.ACTION_VIEW,
            authorizeUrl.toString().toUri()
        ).apply {
            flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_TASK
        }
    }

    fun onAuthorized(code: String) {
        tokenRepository.request(code)
            .doOnSubscribe { _loadingObservable.value = true }
            .doAfterTerminate { _loadingObservable.value = false }
            .subscribe({
                _resultObservable.value = it
            }, {
                _errorObservable.value = it.localizedMessage
            }
            )
            .addTo(disposable)
    }

    override fun onCleared() {
        disposable.clear()
    }
}
