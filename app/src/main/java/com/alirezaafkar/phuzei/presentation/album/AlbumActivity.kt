package com.alirezaafkar.phuzei.presentation.album

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.widget.toast
import com.alirezaafkar.phuzei.R
import com.alirezaafkar.phuzei.data.model.Album
import com.alirezaafkar.phuzei.presentation.base.MvpActivity
import com.alirezaafkar.phuzei.util.InfiniteScrollListener
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
        setupRecycler()
        swipe.setOnRefreshListener { refresh() }
        presenter.getAlbums()
    }

    private fun setupRecycler() {
        adapter = AlbumAdapter(presenter.currentAlbum()) { presenter.selectAlbum(it.id) }
        with(recyclerView) {
            adapter = this@AlbumActivity.adapter
            addOnScrollListener(InfiniteScrollListener { presenter.loadMore() })
        }
    }

    private fun refresh() {
        adapter.clearItems()
        presenter.refresh()
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

    override fun onError(error: String) {
        toast(error).show()
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, AlbumActivity::class.java))
        }
    }
}
