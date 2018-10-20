package com.alirezaafkar.phuzei.presentation.album

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.net.toUri
import com.alirezaafkar.phuzei.App
import com.alirezaafkar.phuzei.MUZEI_PACKAGE_NAME
import com.alirezaafkar.phuzei.R
import com.alirezaafkar.phuzei.data.model.Album
import com.alirezaafkar.phuzei.presentation.base.MvpActivity
import com.alirezaafkar.phuzei.util.InfiniteScrollListener
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_album.*


/**
 * Created by Alireza Afkar on 16/9/2018AD.
 */
class AlbumActivity : MvpActivity<AlbumContract.Presenter>(), AlbumContract.View {
    override val presenter: AlbumContract.Presenter = AlbumPresenter(this)
    override fun getLayout() = R.layout.activity_album

    private lateinit var adapter: AlbumAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        setupRecycler()
        swipe.setOnRefreshListener { refresh() }
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

    private fun setupRecycler() {
        adapter = AlbumAdapter(presenter.currentAlbum()) {
            presenter.selectAlbum(it)
            adapter.setAlbum(it.id)
        }
        with(recyclerView) {
            adapter = this@AlbumActivity.adapter
            addOnScrollListener(InfiniteScrollListener { presenter.loadMore() })
        }
    }

    private fun refresh() {
        adapter.clearItems()
        presenter.refresh()
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

    override fun showLoading() {
        swipe.isRefreshing = true
    }

    override fun hideLoading() {
        swipe.isRefreshing = false
    }

    override fun onAlbums(albums: List<Album>) {
        adapter.addItems(albums)
    }

    override fun onAlbumSelected(title: String) {
        Snackbar.make(
            root,
            getString(R.string.selected_album_, title),
            Snackbar.LENGTH_LONG
        ).setAction(R.string.exit) {
            finish()
        }.show()
    }

    override fun onError(error: String) {
        toast(error)
    }

    override fun loggedOut() {
        App.restart(this)
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, AlbumActivity::class.java))
        }
    }
}
