package com.codding.test.startoverflowuser.repository

import com.codding.test.startoverflowuser.network.ApiService
import com.codding.test.startoverflowuser.network.respond.SoFUser
import com.codding.test.startoverflowuser.network.respond.SoFUserRespond

class SofUserRepository(private val apiService: ApiService) : NetWorkBaseRepository() {

    suspend fun getSofUserData() : MutableList<SoFUser> {
        val sofDataRespond = safeApiCall(
            call = {apiService.getSofUser(1, 50).await()},
            errorMessage = "Get Sof user fail"
        )

        sofDataRespond?.sofUserList?. let {
            return it.toMutableList()
        }
        return mutableListOf()
    }


}