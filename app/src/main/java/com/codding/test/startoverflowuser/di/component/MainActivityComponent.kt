package com.codding.test.startoverflowuser.di.component

import com.codding.test.startoverflowuser.di.module.MainActivityModule
import com.codding.test.startoverflowuser.di.scope.MainActivityScope
import com.codding.test.startoverflowuser.ui.MainActivity
import com.codding.test.startoverflowuser.ui.adapter.SofListAdapter
import dagger.Component

@Component(modules = [MainActivityModule::class], dependencies = [SofComponent::class])
@MainActivityScope
interface MainActivityComponent {
    fun injectMainActivity(mainActivity: MainActivity)
}