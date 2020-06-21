package com.alirezaafkar.phuzei.presentation.login

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.alirezaafkar.phuzei.App
import com.alirezaafkar.phuzei.BuildConfig
import com.alirezaafkar.phuzei.CODE
import com.alirezaafkar.phuzei.ERROR_CODE
import com.alirezaafkar.phuzei.R
import com.alirezaafkar.phuzei.util.toast
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

/**
 * Created by Alireza Afkar on 14/9/2018AD.
 */
class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: LoginViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        App.get(this).component?.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setSupportActionBar(toolbar)
        signIn.setOnClickListener { viewModel.onSignIn() }

        with(viewModel) {
            val owner = this@LoginActivity

            authorizeObservable.observe(owner) {
                startActivity(it)
            }

            loadingObservable.observe(owner) {
                progressBar.isVisible = it
            }

            errorObservable.observe(owner) {
                toast(it)
            }

            resultObservable.observe(owner) {
                App.restart(this@LoginActivity)
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        checkResponse(intent?.data ?: return)
    }

    private fun checkResponse(data: Uri) {
        if (data.scheme?.equals(BuildConfig.APPLICATION_ID) == false) return

        val code = data.getQueryParameter(CODE)
        val error = data.getQueryParameter(ERROR_CODE)

        if (error?.isNotEmpty() == true) {
            toast(getString(R.string.error_code))
            finish()
        } else if (code?.isNotEmpty() == true) {
            viewModel.onAuthorized(code)
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, LoginActivity::class.java))
        }
    }
}
