package com.codding.test.startoverflowuser.viewmodal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.codding.test.startoverflowuser.interator.ReputationIterator
import com.codding.test.startoverflowuser.modal.Reputation
import java.lang.Exception

class ReputationViewModal (private val reputationIterator: ReputationIterator) : ViewModel(), ReputationIterator.ReputationInteratorListener {

    override fun onGetDataError(errorCode: Int, exception: Exception) {

    }

    override fun onGetUserReputationSuccess(repuList: MutableList<Reputation>) {

    }

    override fun onReachedOutOfData() {

    }
}

class ReputationViewModalFactory(private val interator: ReputationIterator) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ReputationViewModalFactory(interator) as T
    }
}