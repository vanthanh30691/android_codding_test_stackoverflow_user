package com.codding.test.startoverflowuser.network

import com.codding.test.startoverflowuser.network.respond.SoFUserRespond
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("users")
    fun getSoFUser(
        @Query("page") page: Int,
        @Query("pagesize") pageSize : Int,
        @Query("site") site : String)
            : Deferred<Response<SoFUserRespond>>
}