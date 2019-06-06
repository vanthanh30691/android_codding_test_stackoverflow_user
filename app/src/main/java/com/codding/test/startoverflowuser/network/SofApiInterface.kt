package com.codding.test.startoverflowuser.network

import com.codding.test.startoverflowuser.network.respond.ReputationRespond
import com.codding.test.startoverflowuser.network.respond.SoFUserRespond
import com.codding.test.startoverflowuser.util.ApiConstant
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SofApiInterface {
    @GET(ApiConstant.API_GET_SOF_USER)
    fun getSoFUser(
        @Query(ApiConstant.API_PAGE_PARAMETER) page: Int,
        @Query(ApiConstant.API_PAGESIZE_PARAMETER) pageSize : Int,
        @Query(ApiConstant.API_SITE_PARAMETER) site : String)
            : Deferred<Response<SoFUserRespond>>

    @GET(ApiConstant.API_USER_REPUTATIONS)
    fun getReputations(
        @Path(ApiConstant.API_USER_REPUTATIONS_INPUT_PATH) userId : String,
        @Query(ApiConstant.API_PAGE_PARAMETER) page: Int,
        @Query(ApiConstant.API_PAGESIZE_PARAMETER) pageSize : Int,
        @Query(ApiConstant.API_SITE_PARAMETER) site : String)
            : Deferred<Response<ReputationRespond>>
}