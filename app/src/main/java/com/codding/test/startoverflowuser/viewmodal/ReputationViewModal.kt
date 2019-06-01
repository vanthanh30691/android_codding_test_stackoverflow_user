package com.codding.test.startoverflowuser.viewmodal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.codding.test.startoverflowuser.interator.ReputationIterator
import com.codding.test.startoverflowuser.modal.Reputation
import com.codding.test.startoverflowuser.screenstate.ReputationState
import com.codding.test.startoverflowuser.screenstate.ScreenState
import com.codding.test.startoverflowuser.util.AppLogger
import java.lang.Exception

class ReputationViewModal (private val reputationIterator: ReputationIterator) : BaseViewModal<ReputationState>(),
    ReputationIterator.ReputationInteratorListener {

    var repuList : MutableList<Reputation> = mutableListOf()
    var lastUserId : String? = null
    var lastPageSize : Int = 1

    fun refreshData() {
        resetPage()
        lastUserId?. let {
            getReputationList(it, lastPageSize)
        }
    }

    fun getReputationList(userId : String, pageSize : Int) {
        AppLogger.debug(this, "getReputationList $userId $pageSize")
        setState(ScreenState.Loading)
        reputationIterator.loadUserReputation(userId, getCurrentPage(), pageSize, this)

        lastPageSize = pageSize
        lastUserId = userId
    }

    override fun onGetDataError(errorCode: Int, exception: Exception) {
        AppLogger.debug(this, "onGetSoFListError")
        postState(ScreenState.Render(ReputationState.LoadRepuError))
    }

    override fun onGetUserReputationSuccess(dataList: MutableList<Reputation>) {
        AppLogger.debug(this, "onGetUserReputationSuccess ${dataList.size}")

        repuList.clear()
        repuList.addAll(dataList)
        postState(ScreenState.Render(ReputationState.LoadRepuDone))
        increasePage()
    }

    override fun onReachedOutOfData() {
        AppLogger.debug(this, "onReachedOutOfData")
        postState(ScreenState.Render(ReputationState.ReachedOutOfData))
    }
}

class ReputationViewModalFactory(private val interator: ReputationIterator) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ReputationViewModal(interator) as T
    }
}