package com.codding.test.startoverflowuser.component

import com.codding.test.startoverflowuser.module.PicassoModule
import com.codding.test.startoverflowuser.module.SofModule
import com.codding.test.startoverflowuser.network.SofApiInterface
import com.codding.test.startoverflowuser.network.SofApiService
import com.squareup.picasso.Picasso
import dagger.Component

@Component (modules = [SofModule::class, PicassoModule::class])
interface SofComponent {
    fun getSofApiService() : SofApiService
    fun getPicasso() : Picasso
}