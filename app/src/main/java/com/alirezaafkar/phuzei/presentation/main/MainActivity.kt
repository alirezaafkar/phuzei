package com.alirezaafkar.phuzei.presentation.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.alirezaafkar.phuzei.App
import com.alirezaafkar.phuzei.MUZEI_PACKAGE_NAME
import com.alirezaafkar.phuzei.R
import com.alirezaafkar.phuzei.presentation.album.AlbumFragment
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

/**
 * Created by Alireza Afkar on 16/9/2018AD.
 */
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: MainViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        App.get(this).component?.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        navigation.setOnNavigationItemSelectedListener {
            val albumType = if (it.itemId == R.id.action_albums) {
                AlbumFragment.TYPE_ALBUMS
            } else {
                AlbumFragment.TYPE_SHARED_ALBUMS
            }
            replaceFragment(albumType)
            true
        }
        navigation.selectedItemId = R.id.action_albums

        viewModel.logoutObservable.observe(this) {
            App.restart(this)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when (item.itemId) {
            R.id.action_sequence -> {
                viewModel.onShuffleOrder(false)
                item.isChecked = true
            }
            R.id.action_shuffle -> {
                viewModel.onShuffleOrder(true)
                item.isChecked = true
            }
            R.id.action_log_out -> viewModel.onLogout()
            R.id.action_muzei -> launchMuzei()
        }
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (viewModel.isShuffleOrder) {
            menu?.findItem(R.id.action_shuffle)?.isChecked = true
        } else {
            menu?.findItem(R.id.action_order)?.isChecked = true
        }
        return super.onPrepareOptionsMenu(menu)
    }

    private fun replaceFragment(albumType: Int) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, AlbumFragment.newInstance(albumType))
            .commit()
    }

    private fun launchMuzei() {
        var intent = packageManager.getLaunchIntentForPackage(MUZEI_PACKAGE_NAME)
        if (intent == null) {
            intent = Intent(Intent.ACTION_VIEW).apply {
                data = "market://details?id=$MUZEI_PACKAGE_NAME".toUri()
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        }
        startActivity(intent)
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }
}
