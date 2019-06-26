package com.codding.test.startoverflowuser.di.module

import com.codding.test.startoverflowuser.di.scope.MainActivityScope
import com.codding.test.startoverflowuser.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder  {
    @MainActivityScope
    @ContributesAndroidInjector
    abstract fun bindMainActivity() : MainActivity
}