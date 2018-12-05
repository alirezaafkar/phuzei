package com.alirezaafkar.phuzei.presentation.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.net.toUri
import com.alirezaafkar.phuzei.App
import com.alirezaafkar.phuzei.MUZEI_PACKAGE_NAME
import com.alirezaafkar.phuzei.R
import com.alirezaafkar.phuzei.presentation.album.AlbumFragment
import com.alirezaafkar.phuzei.presentation.base.MvpActivity
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*


/**
 * Created by Alireza Afkar on 16/9/2018AD.
 */
class MainActivity : MvpActivity<MainContract.Presenter>(), MainContract.View,
    TabLayout.BaseOnTabSelectedListener<TabLayout.Tab> {

    override val presenter: MainContract.Presenter = MainPresenter(this)
    override fun getLayout() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        tabs.addOnTabSelectedListener(this)
        replaceFragment(AlbumFragment.TYPE_ALBUMS)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when {
            item.itemId == R.id.action_sequence -> {
                presenter.setShuffleOrder(false)
                item.isChecked = true
            }
            item.itemId == R.id.action_shuffle -> {
                presenter.setShuffleOrder(true)
                item.isChecked = true
            }
            item.itemId == R.id.action_log_out -> presenter.logout()
            item.itemId == R.id.action_muzei -> launchMuzei()
        }
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (presenter.isShuffleOrder()) {
            menu?.findItem(R.id.action_shuffle)?.isChecked = true
        } else {
            menu?.findItem(R.id.action_order)?.isChecked = true
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    override fun onTabSelected(tab: TabLayout.Tab) {
        val albumType = if (tab.position == 0) {
            AlbumFragment.TYPE_ALBUMS
        } else {
            AlbumFragment.TYPE_SHARED_ALBUMS
        }
        replaceFragment(albumType)
    }

    private fun replaceFragment(albumType: Int) {
        supportFragmentManager
            ?.beginTransaction()
            ?.replace(R.id.container, AlbumFragment.newInstance(albumType))
            ?.commit()
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

    override fun loggedOut() {
        App.restart(this)
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }
}
