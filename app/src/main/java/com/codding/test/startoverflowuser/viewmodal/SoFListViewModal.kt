package com.codding.test.startoverflowuser.viewmodal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.codding.test.startoverflowuser.interator.SoFListIterator
import com.codding.test.startoverflowuser.network.respond.SoFUser
import com.codding.test.startoverflowuser.screenstate.ScreenState
import com.codding.test.startoverflowuser.screenstate.SoFListState

class SoFListViewModal (private val sofInterator: SoFListIterator) : ViewModel(), SoFListIterator.SofListListener {

    private val _sofListState = MutableLiveData<ScreenState<SoFListState>>()
    val sofListState : LiveData<ScreenState<SoFListState>>
            get() = _sofListState

    var sofUser : MutableList<SoFUser> = mutableListOf()

    fun getSofUser() {
        _sofListState.value = ScreenState.Loading
        sofInterator.loadSoFUser(this)
    }

    override fun onGetSoFListFinish(dataList: MutableList<SoFUser>) {
        sofUser.clear()
        sofUser.addAll(dataList)
        _sofListState.postValue(ScreenState.Render(SoFListState.LoadUserDone))
    }

    override fun onGetSoFListError() {
    }
}

class SoFListViewModalFactory(private val sofInterator: SoFListIterator) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SoFListViewModal(sofInterator) as T
    }
}