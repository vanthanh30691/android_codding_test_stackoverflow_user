package com.codding.test.startoverflowuser.repository

import com.codding.test.startoverflowuser.network.ApiService
import com.codding.test.startoverflowuser.network.CustomResult
import com.codding.test.startoverflowuser.network.respond.ReputationRespond

class ReputationRespository (private val apiService: ApiService) : NetWorkBaseRepository() {

    suspend fun getUserReputation(userId : String, page : Int, pageSize: Int) : CustomResult<ReputationRespond> {
        var result = safeApiCall(
            call = { apiService.getUserReputation(userId, page, pageSize).await() }
        )
        return result
    }
}