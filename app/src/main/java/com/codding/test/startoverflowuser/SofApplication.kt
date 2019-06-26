package com.codding.test.startoverflowuser

import android.app.Activity
import android.app.Application
import com.codding.test.startoverflowuser.di.component.DaggerSofComponent
import com.codding.test.startoverflowuser.di.component.SofComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import timber.log.Timber
import javax.inject.Inject

class SofApplication : Application(), HasActivityInjector {


    @Inject lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    private lateinit var sofComponent: SofComponent

    @Override
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        sofComponent = DaggerSofComponent.builder().application(this).build()
        sofComponent.inject(this)
    }

    fun getAppComponent() : SofComponent {
        return sofComponent
    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return activityInjector
    }

}