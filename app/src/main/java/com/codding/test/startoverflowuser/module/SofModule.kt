package com.codding.test.startoverflowuser.module

import com.codding.test.startoverflowuser.network.SofApiInterface
import com.codding.test.startoverflowuser.network.SofApiService
import com.codding.test.startoverflowuser.util.ApiConstant
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module(includes = [OkHttpClientModule::class])
class SofModule {

    @Provides
    fun sofUserApi (retrofit: Retrofit) : SofApiService {
        return SofApiService(retrofit.create(SofApiInterface::class.java))
    }

    @Provides
    fun retrofix(okHttpClient: OkHttpClient, gsonConverterFactory: GsonConverterFactory,
                 coroutineCallAdapterFactory: CoroutineCallAdapterFactory) : Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(ApiConstant.API_BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(coroutineCallAdapterFactory)
            .build()
    }

    @Provides
    fun gson() : Gson {
        return GsonBuilder().create()
    }

    @Provides
    fun gsonConverterFactory(gson: Gson) : GsonConverterFactory {
        return GsonConverterFactory.create(gson)
    }

    @Provides
    fun coroutineCallAdapterFactory() : CoroutineCallAdapterFactory {
        return CoroutineCallAdapterFactory()
    }
}