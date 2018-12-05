package com.alirezaafkar.phuzei.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.alirezaafkar.phuzei.App
import com.alirezaafkar.phuzei.injection.component.MainComponent

/**
 * Created by Alireza Afkar on 6/12/2018AD.
 */
abstract class MvpFragment<out P : BasePresenter<*>> : Fragment(), BaseView {
    abstract val presenter: P

    @LayoutRes
    abstract fun getLayoutRes(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.onCreate()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutRes(), container, false)
    }


    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun mainComponent(): MainComponent = App.get(requireContext()).component!!
}
