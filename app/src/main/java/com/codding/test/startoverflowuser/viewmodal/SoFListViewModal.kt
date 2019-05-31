package com.codding.test.startoverflowuser.viewmodal

import androidx.lifecycle.*
import com.codding.test.startoverflowuser.interator.SoFListIterator
import com.codding.test.startoverflowuser.modal.SoFUser
import com.codding.test.startoverflowuser.screenstate.ScreenState
import com.codding.test.startoverflowuser.screenstate.SoFListState
import com.codding.test.startoverflowuser.util.AppLogger
import java.lang.Exception

class SoFListViewModal (private val sofInterator: SoFListIterator) : ViewModel(), SoFListIterator.SofListListener {

    // Variables to controlled loaded data
    var currentPage = 1

    // Variables to controlled loaded data
    var favoriteSofUserIdList : List<String> = emptyList()

    // Screen state to communicate with Views
    private val _sofListState = MutableLiveData<ScreenState<SoFListState>>()
    val sofListState : LiveData<ScreenState<SoFListState>>
            get() = _sofListState
    var sofUser : MutableList<SoFUser> = mutableListOf()

    init {
        // Get favorite sof list first
        sofInterator.getSofFavoriteUserIdList(this)
    }

    /**
     * Funtions interact with UI
     */
    fun getSofUser(pageSize : Int) {
        AppLogger.debug(this, "getSofUser")
        AppLogger.debug(this, pageSize)

        _sofListState.value = ScreenState.Loading
        sofInterator.loadSoFUser(currentPage, pageSize, this)
    }

    fun toogleFavoriteState(sofUser : SoFUser) {
        AppLogger.debug(this, "toogleFavoriteState")
        AppLogger.debug(this, sofUser)

        sofInterator.toogleFavoriteState(sofUser,this)
    }

    fun getFavoriteUser() {
        AppLogger.debug(this, "getFavoriteUser")

        currentPage = 1
        _sofListState.value = ScreenState.Loading
        sofInterator.getSofFavoriteUsers(this)
    }

    /**
     * Interactor listeners
     */

    override fun onGetSoFListSuccess(dataList: MutableList<SoFUser>) {
        AppLogger.debug(this, "onGetSoFListSuccess")

        sofUser.clear()
        sofUser.addAll(dataList)
        _sofListState.postValue(ScreenState.Render(SoFListState.LoadUserDone))
        currentPage ++
    }

    override fun onGetSoFListError(errorCode : Int, exception: Exception) {
        AppLogger.debug(this, "onGetSoFListError")
        _sofListState.postValue(ScreenState.Render(SoFListState.LoadUserError))

    }

    override fun onReachedOutOfData() {
        AppLogger.debug(this, "onReachedOutOfData")
        _sofListState.postValue(ScreenState.Render(SoFListState.ReachedOutOfData))
    }

    override fun onGetSoFFavoriteIdListSuccess(idList: List<String>) {
        AppLogger.debug(this, "onGetSoFFavoriteIdListSuccess")
        favoriteSofUserIdList = idList
        _sofListState.postValue(ScreenState.Render(SoFListState.LoadFavoriteListDone))
    }

    override fun onGetSoFFavoriteListSuccess(dataList: MutableList<SoFUser>) {
        AppLogger.debug(this, "onGetSoFFavoriteListSuccess")
        sofUser.clear()
        sofUser.addAll(dataList)
        _sofListState.postValue(ScreenState.Render(SoFListState.LoadUserDone))
    }
}

class SoFListViewModalFactory(private val sofInterator: SoFListIterator) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SoFListViewModal(sofInterator) as T
    }
}