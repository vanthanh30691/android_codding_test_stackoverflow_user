package com.codding.test.startoverflowuser.interator

import com.codding.test.startoverflowuser.network.ApiService
import com.codding.test.startoverflowuser.network.respond.SoFUser
import com.codding.test.startoverflowuser.repository.SofUserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class SoFListIterator {

    private val parentJob = Job()
    private val coroutineContext : CoroutineContext
      get() = parentJob + Dispatchers.IO
    private val scope = CoroutineScope(coroutineContext)

    private val repository : SofUserRepository = SofUserRepository(ApiService())


    interface SofListListener {
        fun onGetSoFListSuccess(sofUser: MutableList<SoFUser>)
        fun onGetSoFListError()
    }

    fun loadSoFUser(page : Int, pageSize: Int, listener: SofListListener) {
        scope.launch {
            var userList = repository.getSofUserData(page, pageSize)
            listener.onGetSoFListSuccess(userList)
        }

    }
}