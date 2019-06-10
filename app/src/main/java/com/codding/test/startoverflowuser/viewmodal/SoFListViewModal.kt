package com.codding.test.startoverflowuser.viewmodal

import android.app.Application
import androidx.lifecycle.*
import com.codding.test.startoverflowuser.eventbus.MessageEvent
import com.codding.test.startoverflowuser.modal.SoFUser
import com.codding.test.startoverflowuser.network.CustomResult
import com.codding.test.startoverflowuser.repository.SofUserRepository
import com.codding.test.startoverflowuser.screenstate.ScreenState
import com.codding.test.startoverflowuser.screenstate.SoFListState
import com.codding.test.startoverflowuser.util.EventMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import timber.log.Timber
import java.lang.Exception

class SoFListViewModal (application: Application) : BaseViewModal<SoFListState>(application) {

    // Cache last load page size to refresh data
    private var lastLoadedPageSize = 1

    // Variables to controlled loaded data
    var favoriteSofUserIdList : List<String> = emptyList()
    var sofUser : MutableList<SoFUser> = mutableListOf()

    private var sofUserRepository = SofUserRepository(application)


    fun getFavoriteIdList() {
        viewModelScope.launch(Dispatchers.IO) {
            var favoriteIdList = sofUserRepository.getSofFavoriteUserIdList()
            onGetSoFFavoriteIdListSuccess(favoriteIdList)
        }
    }

    /**
     * Functions interact with UI
     */
    fun getSofUser(pageSize : Int) {
        Timber.d("getSofUser pageSize: $pageSize")
        postState(ScreenState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            var result = sofUserRepository.getSofUserData(getCurrentPage(), pageSize)
            when (result) {
                is CustomResult.Success -> {
                    if (!result.data.hasMore) onReachedOutOfData()
                    else onGetSoFListSuccess(result.data.sofUserList.toMutableList())
                }
                is CustomResult.Error -> onGetDataError(result.errorCode,  result.exception)
            }
        }
        lastLoadedPageSize = pageSize

    }

    fun refreshData(favoriteMode : Boolean) {
        resetPage()
        if (favoriteMode) getFavoriteUser()
        else getSofUser(lastLoadedPageSize)
    }

    fun toogleFavoriteState(sofUser : SoFUser) {
        Timber.d("toogleFavoriteState $sofUser")
        viewModelScope.launch(Dispatchers.IO) {
            var user = sofUserRepository.getUserById(sofUser.userId)
            if (user != null) {
                sofUserRepository.removeFavoriteUser(sofUser)
            } else {
                sofUserRepository.addFavoriteUser(sofUser)
            }
            // Notify new userIdList
            var idList = sofUserRepository.getSofFavoriteUserIdList()
            onGetSoFFavoriteIdListSuccess(idList)
        }
    }

    fun getFavoriteUser() {
        Timber.d("getFavoriteUser")

        resetPage()
        postState(ScreenState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            var userList = sofUserRepository.getSofFavoriteUsers()
            onGetSoFFavoriteListSuccess(userList)
        }
    }

    /**
     * Interactor listeners
     */

    private fun onGetSoFListSuccess(dataList: MutableList<SoFUser>) {
        Timber.d("onGetSoFListSuccess")
        EventBus.getDefault().post(MessageEvent(EventMessage.LOAD_DATA_COMPLETE))

        sofUser.clear()
        sofUser.addAll(dataList)
        postState(ScreenState.Render(SoFListState.LoadUserDone))
        increasePage()
    }

    private fun onGetDataError(errorCode : Int, exception: Exception) {
        Timber.d("onGetSoFListError $errorCode $exception")
        postState(ScreenState.Render(SoFListState.LoadUserError))

    }

    private fun onReachedOutOfData() {
        Timber.d("onReachedOutOfData")
        postState(ScreenState.Render(SoFListState.ReachedOutOfData))
    }

    private fun onGetSoFFavoriteIdListSuccess(idList: List<String>) {
        Timber.d("onGetSoFFavoriteIdListSuccess")
        favoriteSofUserIdList = idList
        postState(ScreenState.Render(SoFListState.LoadFavoriteListDone))
    }

    private fun onGetSoFFavoriteListSuccess(dataList: MutableList<SoFUser>) {
        Timber.d("onGetSoFFavoriteListSuccess")
        sofUser.clear()
        sofUser.addAll(dataList)
        postState(ScreenState.Render(SoFListState.LoadUserDone))
    }
}

