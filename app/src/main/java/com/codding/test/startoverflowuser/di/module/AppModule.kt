package com.codding.test.startoverflowuser.di.module

import android.app.Application
import android.content.Context
import com.codding.test.startoverflowuser.SofApplication
import com.codding.test.startoverflowuser.di.qualifier.ApplicationContext
import dagger.Module
import dagger.Provides
import javax.inject.Inject

@Module
class AppModule {

    @Provides
    @ApplicationContext
    fun provideApplicationContext(app: Application) : Context {
        return app.applicationContext
    }

}