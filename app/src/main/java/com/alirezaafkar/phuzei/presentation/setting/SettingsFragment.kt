package com.alirezaafkar.phuzei.presentation.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.alirezaafkar.phuzei.App
import com.alirezaafkar.phuzei.R
import com.alirezaafkar.phuzei.presentation.muzei.PhotosWorker
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

        with(viewModel) {
            val owner = this@SettingsFragment

            categoryObservable.observe(owner) {
                adapter.setCategory(it)
            }

            intentObservable.observe(owner) {
                startActivity(it)
            }

            isShuffleObservable.observe(owner) {
                if (it) {
                    shuffle.isChecked = true
                } else {
                    sequence.isChecked = true
                }
            }

            logoutObservable.observe(owner) {
                App.restart(requireActivity())
            }

            imagesCountObservable.observe(owner) {
                imagesCountDescription.setSelection(it)
            }

            enqueueImages.observe(owner) {
                PhotosWorker.enqueueLoad(requireContext(), true)
            }
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

        order.setOnCheckedChangeListener { _, id ->
            viewModel.onShuffleOrder(id == R.id.shuffle)
        }

        imagesCount.setOnClickListener { imagesCountDescription.performClick() }
        imagesCountDescription.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                viewModel.onImagesCount(position)
            }

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
