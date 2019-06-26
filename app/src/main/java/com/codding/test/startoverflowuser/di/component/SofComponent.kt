package com.codding.test.startoverflowuser.di.component

import android.app.Application
import com.codding.test.startoverflowuser.SofApplication
import com.codding.test.startoverflowuser.di.module.*
import com.codding.test.startoverflowuser.network.SofApiService
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Component (modules = [SofModule::class, PicassoModule::class, AppModule::class,
    AndroidInjectionModule::class, ActivityBuilder::class, MainActivityModule::class])
@Singleton
interface SofComponent {

    fun getSofApi() : SofApiService

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: Application) : Builder
        fun build() : SofComponent
    }

    fun inject(app : SofApplication)
}