package com.codding.test.startoverflowuser.repository

import com.codding.test.startoverflowuser.network.ApiService
import com.codding.test.startoverflowuser.modal.SoFUser
import com.codding.test.startoverflowuser.network.CustomResult
import com.codding.test.startoverflowuser.network.respond.SoFUserRespond
import com.codding.test.startoverflowuser.roomdatabase.SofUserDatabase
import com.codding.test.startoverflowuser.roomdatabase.dao.SofUserDao

class SofUserRepository(private val apiService: ApiService, private val sofDao : SofUserDao) : NetWorkBaseRepository() {

    suspend fun getSofUserData(page : Int, pageSize: Int) : CustomResult<SoFUserRespond> {
        var result = safeApiCall(
            call = { apiService.getSofUser(page, pageSize).await() }
        )
        return result
    }

    fun getSofFavoriteUsers() : MutableList<SoFUser> {
        return sofDao.getAllFavoriteUser()
    }

    suspend fun addFavoriteUser(sofUser : SoFUser) {
        return sofDao.addUserToFavorite(sofUser)
    }

    suspend fun removeFavoriteUser(sofUser : SoFUser) {
        return sofDao.removeUserFromFavorite(sofUser)
    }

    fun getSofFavoriteUserIdList() : List<String> {
        return sofDao.getSofFavoriteIdList()
    }

    fun getUserById(userId : String) : SoFUser {
        return sofDao.getUserById(userId)
    }

}