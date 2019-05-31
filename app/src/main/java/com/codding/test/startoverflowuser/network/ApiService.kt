package com.codding.test.startoverflowuser.network

import com.codding.test.startoverflowuser.network.respond.SoFUserRespond
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.xml.datatype.DatatypeConstants.SECONDS
import javax.xml.datatype.DatatypeConstants.MINUTES



class ApiService  {

    companion object {
        private const val BASE_URL = "https://api.stackexchange.com/2.2/"
        private const val SITE = "stackoverflow"
    }

    private lateinit  var apiService : ApiInterface

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
        return apiService.getSoFUser(page, pageSize, SITE)
    }


}