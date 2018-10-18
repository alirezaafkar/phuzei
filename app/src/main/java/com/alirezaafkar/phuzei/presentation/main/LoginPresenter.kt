package com.alirezaafkar.phuzei.presentation.main

import com.alirezaafkar.phuzei.data.repository.TokenRepository
import io.reactivex.disposables.CompositeDisposable
import okhttp3.HttpUrl
import javax.inject.Inject

/**
 * Created by Alireza Afkar on 14/9/2018AD.
 */
class LoginPresenter(override val view: LoginContract.View) : LoginContract.Presenter {
    override var disposables: CompositeDisposable? = CompositeDisposable()

    @Inject lateinit var authorizeUrl: HttpUrl
    @Inject lateinit var tokenRepository: TokenRepository

    override fun onCreate() {
        super.onCreate()
        view.mainComponent().inject(this)
    }

    override fun signIn() {
        view.openBrowser(authorizeUrl.toString())
    }

    override fun getToken(code: String) {
        disposables?.add(
            tokenRepository.request(code)
                .doOnSubscribe { view.showLoading() }
                .doAfterTerminate { view.hideLoading() }
                .subscribe(
                    { view.onTokenResult() },
                    { view.onError(it.localizedMessage) }
                )
        )
    }
}
