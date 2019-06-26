package com.codding.test.startoverflowuser.repository

import android.app.Application
import android.util.Log
import com.codding.test.startoverflowuser.SofApplication
import com.codding.test.startoverflowuser.network.CustomResult
import com.codding.test.startoverflowuser.network.SofApiService
import retrofit2.Response
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject

open class NetWorkBaseRepository (application : Application) {


    @Inject
    lateinit var apiService : SofApiService

    init {
        var sofApp = application as SofApplication
        apiService = sofApp.getAppComponent().getSofApi()
    }

    suspend fun <T: Any> safeApiCall(call: suspend () -> Response<T>) : CustomResult<T> {
        var respond = call.invoke()
        if (respond.isSuccessful) return CustomResult.Success(respond.body()!!)
        return CustomResult.Error(respond.code(), IOException(respond.message()))
    }
}