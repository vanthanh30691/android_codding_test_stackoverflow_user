package com.codding.test.startoverflowuser.repository

import com.codding.test.startoverflowuser.network.ApiService
import com.codding.test.startoverflowuser.modal.SoFUser

class SofUserRepository(private val apiService: ApiService) : NetWorkBaseRepository() {

    suspend fun getSofUserData(page : Int, pageSize: Int) : MutableList<SoFUser> {
        val sofDataRespond = safeApiCall(
            call = {apiService.getSofUser(page, pageSize).await()},
            errorMessage = "Get Sof user fail"
        )

        sofDataRespond?.sofUserList?. let {
            return it.toMutableList()
        }
        return mutableListOf()
    }


}