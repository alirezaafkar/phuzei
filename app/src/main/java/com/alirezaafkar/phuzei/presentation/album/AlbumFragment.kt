package com.alirezaafkar.phuzei.presentation.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.alirezaafkar.phuzei.App
import com.alirezaafkar.phuzei.BuildConfig
import com.alirezaafkar.phuzei.MUZEI_PACKAGE_NAME
import com.alirezaafkar.phuzei.R
import com.alirezaafkar.phuzei.presentation.main.AlbumAdapter
import com.alirezaafkar.phuzei.presentation.muzei.PhotosWorker
import com.alirezaafkar.phuzei.util.InfiniteScrollListener
import com.alirezaafkar.phuzei.util.isMuzeiInstalled
import com.alirezaafkar.phuzei.util.openInPlayStore
import com.alirezaafkar.phuzei.util.toast
import com.google.android.apps.muzei.api.MuzeiContract
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_albums.*
import javax.inject.Inject

/**
 * Created by Alireza Afkar on 6/12/2018AD.
 */
class AlbumFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: AlbumViewModel by viewModels { viewModelFactory }

    private lateinit var adapter: AlbumAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        App.get(requireContext()).component?.inject(this)

        super.onCreate(savedInstanceState)

        with(viewModel) {
            val owner = this@AlbumFragment
            selectAlbumObservable.observe(owner) {
                if (isPhuzeiSelected()) {
                    showExitSnackBar(it)
                } else {
                    showActivateSnackBar()
                }
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

            enqueueImages.observe(owner) {
                PhotosWorker.enqueueLoad(requireContext(), true)
            }
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
        viewModel.subscribe(arguments?.getInt(KEY_TYPE, TYPE_ALBUMS) ?: TYPE_ALBUMS)
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

    private fun isPhuzeiSelected(): Boolean {
        return MuzeiContract.Sources.isProviderSelected(
            requireContext(),
            BuildConfig.PHUZEI_AUTHORITY
        )
    }

    private fun showActivateSnackBar() {
        Snackbar.make(
            swipe,
            R.string.source_not_activated,
            Snackbar.LENGTH_LONG
        ).setAction(R.string.activate) {
            val intent = MuzeiContract.Sources.createChooseProviderIntent(BuildConfig.PHUZEI_AUTHORITY)
            if (isMuzeiInstalled()) {
                startActivity(intent)
            } else {
                openInPlayStore(MUZEI_PACKAGE_NAME)
            }
        }.show()
    }

    private fun showExitSnackBar(title: String) {
        Snackbar.make(
            swipe,
            getString(R.string.selected_album_, title),
            Snackbar.LENGTH_LONG
        ).setAction(R.string.exit) {
            requireActivity().finish()
        }.show()
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
