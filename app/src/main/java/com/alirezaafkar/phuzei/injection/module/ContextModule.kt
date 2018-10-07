package com.alirezaafkar.phuzei.injection.module

import android.content.Context
import com.alirezaafkar.phuzei.App
import dagger.Module
import dagger.Provides
import com.alirezaafkar.phuzei.injection.qualifier.ApplicationContext
import com.alirezaafkar.phuzei.injection.scope.ApplicationScope

/**
 * Created by Alireza Afkar on 16/3/2018AD.
 */
@Module
class ContextModule(val context: Context) {
    @Provides
    fun provideContext() = context

    @Provides
    @ApplicationScope
    @ApplicationContext
    fun provideApplication(): App = context.applicationContext as App
}
