package com.codding.test.startoverflowuser.repository

import android.util.Log
import com.codding.test.startoverflowuser.network.CustomResult
import retrofit2.Response
import java.io.IOException
import java.lang.Exception

open class NetWorkBaseRepository {

    suspend fun <T: Any> safeApiCall(call: suspend () -> Response<T>) : CustomResult<T> {
        var respond = call.invoke()
        if (respond.isSuccessful) return CustomResult.Success(respond.body()!!)
        return CustomResult.Error(respond.code(), IOException(respond.message()))
    }
}