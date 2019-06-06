package com.codding.test.startoverflowuser.di.module

import com.codding.test.startoverflowuser.ui.adapter.SofListAdapter
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule {
    @Provides
    fun sofListAdapter(picasso: Picasso) : SofListAdapter {
        return SofListAdapter(picasso)
    }
}