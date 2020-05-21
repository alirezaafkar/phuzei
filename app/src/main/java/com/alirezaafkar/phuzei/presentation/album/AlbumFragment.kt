package com.alirezaafkar.phuzei.presentation.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.alirezaafkar.phuzei.App
import com.alirezaafkar.phuzei.R
import com.alirezaafkar.phuzei.presentation.main.AlbumAdapter
import com.alirezaafkar.phuzei.util.InfiniteScrollListener
import com.alirezaafkar.phuzei.util.toast
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_albums.*
import javax.inject.Inject

/**
 * Created by Alireza Afkar on 6/12/2018AD.
 */
class AlbumFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: AlbumViewModel by activityViewModels { viewModelFactory }

    private lateinit var adapter: AlbumAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        App.get(requireContext()).component?.inject(this)

        super.onCreate(savedInstanceState)

        with(viewModel) {
            val owner = this@AlbumFragment
            selectAlbumObservable.observe(owner) {
                Snackbar.make(
                    swipe,
                    getString(R.string.selected_album_, it),
                    Snackbar.LENGTH_LONG
                ).setAction(R.string.exit) {
                    requireActivity().finish()
                }.show()
            }

            albumsObservable.observe(owner) {
                if (it.isNotEmpty()) {
                    adapter.addItems(it)
                }
            }

            loadingObservable.observe(owner) {
                swipe.isRefreshing = it
            }

            errorObservable.observe(owner) {
                requireContext().toast(it)
            }

            subscribe(arguments?.getInt(KEY_TYPE, TYPE_ALBUMS) ?: TYPE_ALBUMS)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.fragment_albums, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipe.setOnRefreshListener { refresh() }
        setupRecycler()
    }

    private fun setupRecycler() {
        adapter = AlbumAdapter(viewModel.currentAlbum) {
            viewModel.onSelectAlbum(it)
            adapter.setAlbum(it.id)
        }

        with(recyclerView) {
            adapter = this@AlbumFragment.adapter
            addOnScrollListener(InfiniteScrollListener { viewModel.onLoadMore() })
        }
    }

    private fun refresh() {
        adapter.clearItems()
        viewModel.onRefresh()
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
