package com.codding.test.startoverflowuser.repository

import android.app.Application
import com.codding.test.startoverflowuser.network.SofApiService
import com.codding.test.startoverflowuser.network.CustomResult
import com.codding.test.startoverflowuser.network.respond.ReputationRespond

class ReputationRespository(application: Application) : NetWorkBaseRepository(application) {

    suspend fun getUserReputation(userId : String, page : Int, pageSize: Int) : CustomResult<ReputationRespond> {
        var result = safeApiCall(
            call = { getApiService().getUserReputation(userId, page, pageSize).await() }
        )
        return result
    }
}