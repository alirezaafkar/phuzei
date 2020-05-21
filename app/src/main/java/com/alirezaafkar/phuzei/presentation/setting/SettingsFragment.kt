package com.alirezaafkar.phuzei.presentation.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.alirezaafkar.phuzei.App
import com.alirezaafkar.phuzei.R
import kotlinx.android.synthetic.main.fragment_settings.*
import javax.inject.Inject

/**
 * Created by Alireza Afkar on 5/21/20.
 */
class SettingsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: SettingsViewModel by activityViewModels { viewModelFactory }

    private lateinit var adapter: SettingsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        App.get(requireContext()).component?.inject(this)
        super.onCreate(savedInstanceState)

        viewModel.categoryObservable.observe(this) {
            adapter.setCategory(it)
        }

        viewModel.intentObservable.observe(this) {
            startActivity(it)
        }

        viewModel.isShuffleObservable.observe(this) {
            if (it) {
                sequence.isChecked = true
            } else {
                shuffle.isChecked = true
            }
        }

        viewModel.logoutObservable.observe(this) {
            App.restart(requireActivity())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        contact.setOnClickListener { viewModel.onContact() }
        contactDescription.setOnClickListener { viewModel.onContact() }

        muzei.setOnClickListener { viewModel.onMuzeiClick(requireContext().packageManager) }
        muzeiDescription.setOnClickListener { viewModel.onMuzeiClick(requireContext().packageManager) }

        logout.setOnClickListener { viewModel.onLogout() }
        logoutDescription.setOnClickListener { viewModel.onLogout() }

        order.setOnCheckedChangeListener { _, index ->
            viewModel.onShuffleOrder(index != 0)
        }

        setupRecycler()

        viewModel.subscribe()
    }

    private fun setupRecycler() {
        adapter = SettingsAdapter {
            viewModel.onSelectCategory(it)
        }
        categories.adapter = adapter
    }

    companion object {
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }
}
