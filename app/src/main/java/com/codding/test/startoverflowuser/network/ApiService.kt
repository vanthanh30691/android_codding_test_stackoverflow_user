package com.codding.test.startoverflowuser.network

import com.codding.test.startoverflowuser.modal.Reputation
import com.codding.test.startoverflowuser.network.respond.ReputationRespond
import com.codding.test.startoverflowuser.network.respond.SoFUserRespond
import com.codding.test.startoverflowuser.util.ApiConstant
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiService  {

    companion object {
        private const val BASE_URL = "https://api.stackexchange.com/2.2/"
    }

    private var apiService : ApiInterface

    init {
        apiService = getApiService(retrofix())
    }

    private fun getApiService(retrofit: Retrofit) = retrofit.create(ApiInterface::class.java)

    private fun retrofix() : Retrofit = Retrofit.Builder()
        .client(OkHttpClient())
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    fun getSofUser(page : Int, pageSize : Int) : Deferred<Response<SoFUserRespond>> {
        return apiService.getSoFUser(page, pageSize, ApiConstant.API_DEFAULT_SITE_PARAMETER)
    }

    fun getUserReputation(userId : String, page : Int, pageSize : Int) : Deferred<Response<ReputationRespond>> {
        return apiService.getReputations(userId, page, pageSize, ApiConstant.API_DEFAULT_SITE_PARAMETER)
    }


}