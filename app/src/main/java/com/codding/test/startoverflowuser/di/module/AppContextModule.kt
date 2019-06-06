package com.codding.test.startoverflowuser.di.module

import android.content.Context
import com.codding.test.startoverflowuser.di.qualifier.ApplicationContext
import dagger.Module
import dagger.Provides

@Module
class AppContextModule(private val context : Context) {

    @ApplicationContext
    @Provides
    fun context() : Context {
        return context.applicationContext
    }
}