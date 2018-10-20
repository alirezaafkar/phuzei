package com.alirezaafkar.phuzei.presentation.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import androidx.core.net.toUri
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.alirezaafkar.phuzei.*
import com.alirezaafkar.phuzei.presentation.base.MvpActivity
import kotlinx.android.synthetic.main.activity_login.*


/**
 * Created by Alireza Afkar on 14/9/2018AD.
 */
class LoginActivity : MvpActivity<LoginContract.Presenter>(), LoginContract.View {
    override val presenter: LoginContract.Presenter = LoginPresenter(this)
    override fun getLayout() = R.layout.activity_login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(toolbar)
        signIn.setOnClickListener { presenter.signIn() }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        intent?.data?.let {
            checkResponse(it)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.setGroupVisible(R.id.action_order, false)
        menu?.setGroupVisible(R.id.action_other, false)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun openBrowser(url: String) {
        Intent(Intent.ACTION_VIEW, url.toUri()).apply {
            flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_TASK
        }.also {
            startActivity(it)
        }
    }

    private fun checkResponse(data: Uri) {
        if (data.scheme?.equals(BASE_REDIRECT_URI) == false) return

        val code = data.getQueryParameter(CODE)
        val error = data.getQueryParameter(ERROR_CODE)

        if (error?.isNotEmpty() == true) {
            onError(getString(R.string.error_code))
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
        toast(error)
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, LoginActivity::class.java))
        }
    }
}
