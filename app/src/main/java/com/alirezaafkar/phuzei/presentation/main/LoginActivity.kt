package com.alirezaafkar.phuzei.presentation.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.alirezaafkar.phuzei.*
import com.alirezaafkar.phuzei.presentation.base.MvpActivity
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.HttpUrl


/**
 * Created by Alireza Afkar on 14/9/2018AD.
 */
class LoginActivity : MvpActivity<LoginContract.Presenter>(), LoginContract.View {
    override val presenter: LoginContract.Presenter = LoginPresenter(this)
    override fun getLayout() = R.layout.activity_login

    /*@Inject
    @AuthorizeUrl
    lateinit var authorizeUrl: HttpUrl*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainComponent().inject(this)
        signIn.setOnClickListener { presenter.signIn() }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        intent?.data?.let {
            checkResponse(it)
        }
    }

    override fun openBrowser() {
        val authorizeUrl = HttpUrl.Builder()
            .scheme(SCHEME)
            .host(BASE_URL)
            .addPathSegments("o/oauth2/v2/auth")
            .addQueryParameter(KEY_SCOPE, API_SCOPE)
            .addQueryParameter(KEY_RESPONSE_TYPE, CODE)
            .addQueryParameter(KEY_CLIENT_ID, CLIENT_ID)
            .addQueryParameter(KEY_REDIRECT_URI, REDIRECT_URI)
            .build()

        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(authorizeUrl.toString())
            flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    }

    private fun checkResponse(data: Uri) {
        if (data.scheme?.equals(BASE_REDIRECT_URI) == false) return

        val code = data.getQueryParameter(CODE)
        val error = data.getQueryParameter(ERROR_CODE)

        if (error?.isNotEmpty() == true) {
            onError(getString(R.string.errode_code))
            finish()
        } else if (code?.isNotEmpty() == true) {
            presenter.getToken(code)
        }
    }

    override fun showLoading() {
        progressBar.isVisible = true
    }

    override fun hideLoading() {
        progressBar.isGone = true

    }

    override fun onTokenResult() {
        App.restart(this)
    }

    override fun onError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show()
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, LoginActivity::class.java))
        }
    }
}
