package com.codding.test.startoverflowuser.di.component

import com.codding.test.startoverflowuser.di.module.PicassoModule
import com.codding.test.startoverflowuser.di.module.SofModule
import com.codding.test.startoverflowuser.network.SofApiService
import com.squareup.picasso.Picasso
import dagger.Component
import javax.inject.Singleton

@Component (modules = [SofModule::class, PicassoModule::class])
@Singleton
interface SofComponent {
    fun getSofApiService() : SofApiService
    fun getPicasso() : Picasso
}