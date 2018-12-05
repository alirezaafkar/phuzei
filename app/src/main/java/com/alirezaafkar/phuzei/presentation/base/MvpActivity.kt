package com.alirezaafkar.phuzei.presentation.base

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.alirezaafkar.phuzei.App
import com.alirezaafkar.phuzei.R
import com.alirezaafkar.phuzei.injection.component.MainComponent


/**
 * Created by Alireza Afkar on 16/3/2018AD.
 */
abstract class MvpActivity<out P : BasePresenter<*>> : AppCompatActivity(), BaseView {
    abstract val presenter: P

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
        presenter.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_info) {
            sendEmail()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    @LayoutRes
    abstract fun getLayout(): Int

    override fun mainComponent(): MainComponent = App.get(this).component!!

    private fun sendEmail() {
        Intent(Intent.ACTION_SENDTO, "mailto:".toUri()).apply {
            putExtra(Intent.EXTRA_EMAIL, arrayOf("pesiran@gmail.com"))
            putExtra(Intent.EXTRA_SUBJECT, "Phuzei")
        }.also {
            startActivity(it)
        }
    }

}
