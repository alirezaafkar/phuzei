package com.alirezaafkar.phuzei.presentation.album

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.alirezaafkar.phuzei.R
import com.alirezaafkar.phuzei.data.model.Album
import com.alirezaafkar.phuzei.presentation.base.MvpFragment
import com.alirezaafkar.phuzei.presentation.main.AlbumAdapter
import com.alirezaafkar.phuzei.util.InfiniteScrollListener
import com.alirezaafkar.phuzei.util.toast
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_albums.*

/**
 * Created by Alireza Afkar on 6/12/2018AD.
 */
class AlbumFragment : MvpFragment<AlbumContract.Presenter>(), AlbumContract.View {
    override val presenter: AlbumContract.Presenter = AlbumPresenter(this)
    override fun getLayoutRes() = R.layout.fragment_albums

    private lateinit var adapter: AlbumAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.setAlbumType(arguments?.getInt(KEY_TYPE, TYPE_ALBUMS) ?: TYPE_ALBUMS)
        presenter.getAlbums()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipe.setOnRefreshListener { refresh() }
        setupRecycler()
    }

    private fun setupRecycler() {
        adapter = AlbumAdapter(presenter.currentAlbum()) {
            presenter.selectAlbum(it)
            adapter.setAlbum(it.id)
        }

        with(recyclerView) {
            adapter = this@AlbumFragment.adapter
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

    override fun loadAlbums() {
        presenter.loadMore()
    }

    override fun onAlbumSelected(title: String) {
        Snackbar.make(
            swipe,
            getString(R.string.selected_album_, title),
            Snackbar.LENGTH_LONG
        ).setAction(R.string.exit) {
            requireActivity().finish()
        }.show()
    }

    override fun onError(error: String) {
        context?.toast(error)
    }

    companion object {
        private const val KEY_TYPE = "type"

        const val TYPE_ALBUMS = 0
        const val TYPE_SHARED_ALBUMS = 1

        fun newInstance(type: Int): AlbumFragment {
            return AlbumFragment().apply {
                arguments = bundleOf(KEY_TYPE to type)
            }
        }
    }
}
