package com.codding.test.startoverflowuser.interator

import android.app.Application
import com.codding.test.startoverflowuser.network.ApiService
import com.codding.test.startoverflowuser.modal.SoFUser
import com.codding.test.startoverflowuser.network.CustomResult
import com.codding.test.startoverflowuser.repository.SofUserRepository
import com.codding.test.startoverflowuser.roomdatabase.SofUserDatabase
import kotlinx.coroutines.*
import java.lang.Appendable
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class SoFListIterator(application : Application) {

    // Coroutine components
    private val parentJob = Job()
    private val coroutineContext : CoroutineContext
        get() = parentJob + Dispatchers.IO
    private val scope = CoroutineScope(coroutineContext)

    // Data Repository
    private var repository : SofUserRepository

    init {
        val sofUserDao = SofUserDatabase.getDatabase(application).sofUserDao()
        repository  = SofUserRepository(ApiService(), sofUserDao)
    }

    interface SofListListener {
        fun onReachedOutOfData()
        fun onGetSoFListSuccess(sofUser: MutableList<SoFUser>)
        fun onGetSoFFavoriteListSuccess(sofUser: MutableList<SoFUser>)
        fun onGetSoFFavoriteIdListSuccess(idList: List<String>)
        fun onGetSoFListError(errorCode : Int, exception: Exception)
    }

    fun loadSoFUser(page : Int, pageSize: Int, listener: SofListListener) {
        scope.launch {
            var result = repository.getSofUserData(page, pageSize)
            when (result) {
                is CustomResult.Success -> {
                    if (!result.data.hasMore) listener.onReachedOutOfData()
                    else listener.onGetSoFListSuccess(result.data.sofUserList.toMutableList())
                }
                is CustomResult.Error -> listener.onGetSoFListError(result.errorCode,  result.exception)
            }

        }
    }

    fun getSofFavoriteUsers(listener: SofListListener) {
        scope.launch {
            var userList = repository.getSofFavoriteUsers()
            listener.onGetSoFFavoriteListSuccess(userList)
        }
    }


    fun getSofFavoriteUserIdList(listener: SofListListener) {
        scope.launch {
            var idList = repository.getSofFavoriteUserIdList()
            listener.onGetSoFFavoriteIdListSuccess(idList)
        }
    }

    fun toogleFavoriteState(sofUser : SoFUser, listener: SofListListener) {
        scope.launch {
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