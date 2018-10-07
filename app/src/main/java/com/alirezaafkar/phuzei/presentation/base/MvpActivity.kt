package com.alirezaafkar.phuzei.presentation.base

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import com.alirezaafkar.phuzei.App
import com.alirezaafkar.phuzei.injection.component.MainComponent


/**
 * Created by Alireza Afkar on 16/3/2018AD.
 */
abstract class MvpActivity<out P : BasePresenter<*>> : AppCompatActivity(), BaseView {
    abstract val presenter: P

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
        presenter.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    @LayoutRes
    abstract fun getLayout(): Int

    override fun mainComponent(): MainComponent = App.get(this).component!!
}
