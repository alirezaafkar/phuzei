package com.alirezaafkar.phuzei

import android.app.Application
import android.content.Context
import android.content.Intent
import com.alirezaafkar.phuzei.presentation.splash.SplashActivity
import com.alirezaafkar.phuzei.injection.component.DaggerMainComponent
import com.alirezaafkar.phuzei.injection.component.MainComponent
import com.alirezaafkar.phuzei.injection.module.ContextModule
import com.alirezaafkar.phuzei.presentation.base.MvpActivity

/**
 * Created by Alireza Afkar on 14/9/2018AD.
 */
class App : Application() {
    var component: MainComponent? = null
        get() {
            if (field == null) {
                field = DaggerMainComponent.builder()
                    .contextModule(ContextModule(this)).build()
            }
            return field
        }

    companion object {
        fun get(context: Context): App {
            return context.applicationContext as App
        }

        fun restart(activity: MvpActivity<*>) {
            get(activity).component = null

            val intent = Intent(activity.applicationContext, SplashActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            activity.startActivity(intent)
            activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
}
