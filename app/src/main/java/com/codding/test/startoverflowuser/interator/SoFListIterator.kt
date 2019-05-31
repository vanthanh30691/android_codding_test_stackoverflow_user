package com.codding.test.startoverflowuser.interator

import android.app.Application
import com.codding.test.startoverflowuser.listener.BaseIteratorListener
import com.codding.test.startoverflowuser.network.ApiService
import com.codding.test.startoverflowuser.modal.SoFUser
import com.codding.test.startoverflowuser.network.CustomResult
import com.codding.test.startoverflowuser.repository.SofUserRepository
import com.codding.test.startoverflowuser.roomdatabase.SofUserDatabase
import kotlinx.coroutines.*

class SoFListIterator(application : Application) : BaseIterator(application) {


    // Data Repository
    private var repository : SofUserRepository

    init {
        val sofUserDao = SofUserDatabase.getDatabase(application).sofUserDao()
        repository  = SofUserRepository(ApiService(), sofUserDao)
    }

    interface SofListListener : BaseIteratorListener {
        fun onGetSoFListSuccess(sofUser: MutableList<SoFUser>)
        fun onGetSoFFavoriteListSuccess(sofUser: MutableList<SoFUser>)
        fun onGetSoFFavoriteIdListSuccess(idList: List<String>)
    }

    fun loadSoFUser(page : Int, pageSize: Int, listener: SofListListener) {
        coroutineScope.launch {
            var result = repository.getSofUserData(page, pageSize)
            when (result) {
                is CustomResult.Success -> {
                    if (!result.data.hasMore) listener.onReachedOutOfData()
                    else listener.onGetSoFListSuccess(result.data.sofUserList.toMutableList())
                }
                is CustomResult.Error -> listener.onGetDataError(result.errorCode,  result.exception)
            }

        }
    }

    fun getSofFavoriteUsers(listener: SofListListener) {
        coroutineScope.launch {
            var userList = repository.getSofFavoriteUsers()
            listener.onGetSoFFavoriteListSuccess(userList)
        }
    }


    fun getSofFavoriteUserIdList(listener: SofListListener) {
        coroutineScope.launch {
            var idList = repository.getSofFavoriteUserIdList()
            listener.onGetSoFFavoriteIdListSuccess(idList)
        }
    }

    fun toogleFavoriteState(sofUser : SoFUser, listener: SofListListener) {
        coroutineScope.launch {
            var user = repository.getUserById(sofUser.userId)
            if (user != null) {
                repository.removeFavoriteUser(sofUser)
            } else {
                repository.addFavoriteUser(sofUser)
            }
            // Notify new userIdList
            var idList = repository.getSofFavoriteUserIdList()
            listener.onGetSoFFavoriteIdListSuccess(idList)
        }
    }
}