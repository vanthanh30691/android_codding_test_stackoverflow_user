package com.codding.test.startoverflowuser.viewmodal

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.codding.test.startoverflowuser.modal.Reputation
import com.codding.test.startoverflowuser.network.CustomResult
import com.codding.test.startoverflowuser.repository.ReputationRespository
import com.codding.test.startoverflowuser.screenstate.ReputationState
import com.codding.test.startoverflowuser.screenstate.ScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

class ReputationViewModal (application: Application) : BaseViewModal<ReputationState>(application) {

    var repuList : MutableList<Reputation> = mutableListOf()


    private var lastUserId : String? = null
    private var lastPageSize : Int = 1
    private var reputationRespository = ReputationRespository(application)

    fun refreshData() {
        resetPage()
        lastUserId?. let {
            getReputationList(it, lastPageSize)
        }
    }

    fun getReputationList(userId : String, pageSize : Int) {
        Timber.d("getReputationList $userId $pageSize")
        postState(ScreenState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            var result = reputationRespository.getUserReputation(userId, getCurrentPage(), pageSize)
            when (result) {
                is CustomResult.Success -> {
                    if (!result.data.hasMore) onReachedOutOfData()
                    else onGetUserReputationSuccess(result.data.repuList.toMutableList())
                }
                is CustomResult.Error -> onGetDataError(result.errorCode,  result.exception)
            }
        }
        lastPageSize = pageSize
        lastUserId = userId
    }

    private fun onGetDataError(errorCode: Int, exception: Exception) {
        Timber.d("onGetSoFListError $errorCode $exception")
        postState(ScreenState.Render(ReputationState.LoadRepuError))
    }

    private fun onGetUserReputationSuccess(dataList: MutableList<Reputation>) {
        Timber.d("onGetUserReputationSuccess ${dataList.size}")

        repuList.clear()
        repuList.addAll(dataList)
        postState(ScreenState.Render(ReputationState.LoadRepuDone))
        increasePage()
    }

    private fun onReachedOutOfData() {
        Timber.d("onReachedOutOfData")
        postState(ScreenState.Render(ReputationState.ReachedOutOfData))
    }
}

