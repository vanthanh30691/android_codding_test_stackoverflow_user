package com.codding.test.startoverflowuser.interator

import android.app.Application
import com.codding.test.startoverflowuser.listener.BaseIteratorListener
import com.codding.test.startoverflowuser.modal.Reputation
import com.codding.test.startoverflowuser.network.ApiService
import com.codding.test.startoverflowuser.network.CustomResult
import com.codding.test.startoverflowuser.repository.ReputationRespository
import kotlinx.coroutines.launch

class ReputationIterator(application : Application) : BaseIterator(application) {

    // Data Repository
    private var repository : ReputationRespository

    init {
        repository  = ReputationRespository(ApiService())
    }

    interface ReputationInteratorListener : BaseIteratorListener {
        fun onGetUserReputationSuccess(repuList: MutableList<Reputation>)
    }

    fun loadUserReputation(userId : String, page : Int, pageSize: Int, listener: ReputationInteratorListener) {
        coroutineScope.launch {
            var result = repository.getUserReputation(userId, page, pageSize)
            when (result) {
                is CustomResult.Success -> {
                    if (!result.data.hasMore) listener.onReachedOutOfData()
                    else listener.onGetUserReputationSuccess(result.data.repuList.toMutableList())
                }
                is CustomResult.Error -> listener.onGetDataError(result.errorCode,  result.exception)
            }

        }
    }

}