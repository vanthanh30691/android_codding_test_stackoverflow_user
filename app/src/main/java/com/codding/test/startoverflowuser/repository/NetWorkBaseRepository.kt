package com.codding.test.startoverflowuser.repository

import android.util.Log
import com.codding.test.startoverflowuser.network.CustomResult
import retrofit2.Response
import java.io.IOException

open class NetWorkBaseRepository {
    suspend fun<T : Any> safeApiCall(call : suspend () -> Response<T>, errorMessage : String) : T? {

        val result : CustomResult<T> = safeApiResult(call, errorMessage)
        var data : T? = null

        when (result) {
            is CustomResult.Success -> data = result.data
            is CustomResult.Error -> {  Log.d("NetWorkBaseRepository", "$errorMessage & Exception - ${result.exception}")}
        }

        return data
    }

    private suspend fun <T: Any> safeApiResult(call: suspend () -> Response<T>, errorMessage: String) : CustomResult<T> {
        var respond = call.invoke()
        if (respond.isSuccessful) return CustomResult.Success(respond.body()!!)

        return CustomResult.Error(IOException("$errorMessage"))
    }
}