package com.codding.test.startoverflowuser

import android.app.Application
import timber.log.Timber

class SofApplication : Application() {

    @Override
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}