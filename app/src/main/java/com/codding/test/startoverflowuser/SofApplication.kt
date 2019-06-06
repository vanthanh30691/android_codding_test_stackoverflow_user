package com.codding.test.startoverflowuser

import android.app.Application
import com.codding.test.startoverflowuser.di.component.DaggerSofComponent
import com.codding.test.startoverflowuser.di.component.SofComponent
import com.codding.test.startoverflowuser.di.module.AppContextModule
import timber.log.Timber

class SofApplication : Application() {

    private lateinit var sofComponent: SofComponent

    @Override
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        sofComponent = DaggerSofComponent.builder().appContextModule(AppContextModule(this))
            .build()
    }

    fun getAppComponent() : SofComponent {
        return sofComponent
    }
}