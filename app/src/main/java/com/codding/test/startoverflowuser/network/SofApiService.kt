package com.codding.test.startoverflowuser.network

import com.codding.test.startoverflowuser.network.respond.ReputationRespond
import com.codding.test.startoverflowuser.network.respond.SoFUserRespond
import com.codding.test.startoverflowuser.util.ApiConstant
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.*


class SofApiService(private val softApiInterface: SofApiInterface)  {

    init {
        Timber.d("Create new SofApiService")
    }

    fun getSofUser(page : Int, pageSize : Int) : Deferred<Response<SoFUserRespond>> {
        return softApiInterface.getSoFUser(page, pageSize, ApiConstant.API_DEFAULT_SITE_PARAMETER)
    }

    fun getUserReputation(userId : String, page : Int, pageSize : Int) : Deferred<Response<ReputationRespond>> {
        return softApiInterface.getReputations(userId, page, pageSize, ApiConstant.API_DEFAULT_SITE_PARAMETER)
    }

}