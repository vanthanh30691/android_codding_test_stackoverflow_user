package com.codding.test.startoverflowuser.viewmodal

import androidx.lifecycle.*
import com.codding.test.startoverflowuser.eventbus.MessageEvent
import com.codding.test.startoverflowuser.interator.SoFListIterator
import com.codding.test.startoverflowuser.modal.SoFUser
import com.codding.test.startoverflowuser.screenstate.ScreenState
import com.codding.test.startoverflowuser.screenstate.SoFListState
import com.codding.test.startoverflowuser.util.EventMessage
import org.greenrobot.eventbus.EventBus
import timber.log.Timber
import java.lang.Exception

class SoFListViewModal (private val sofInterator: SoFListIterator) : BaseViewModal<SoFListState>(), SoFListIterator.SofListListener {


    // Cache last load page size to refresh data
    var lastLoadedPageSize = 1

    // Variables to controlled loaded data
    var favoriteSofUserIdList : List<String> = emptyList()
    var sofUser : MutableList<SoFUser> = mutableListOf()


    init {
        // Get favorite sof list first
        sofInterator.getSofFavoriteUserIdList(this)
    }

    /**
     * Funtions interact with UI
     */
    fun getSofUser(pageSize : Int) {
        Timber.d("getSofUser pageSize: $pageSize")

        postState(ScreenState.Loading)
        sofInterator.loadSoFUser(getCurrentPage(), pageSize, this)
        lastLoadedPageSize = pageSize

    }

    fun refreshData(favoriteMode : Boolean) {
        resetPage()
        if (favoriteMode) getFavoriteUser()
        else getSofUser(lastLoadedPageSize)
    }

    fun toogleFavoriteState(sofUser : SoFUser) {
        Timber.d("toogleFavoriteState $sofUser")
        sofInterator.toogleFavoriteState(sofUser,this)
    }

    fun getFavoriteUser() {
        Timber.d("getFavoriteUser")

        resetPage()
        postState(ScreenState.Loading)
        sofInterator.getSofFavoriteUsers(this)
    }

    /**
     * Interactor listeners
     */

    override fun onGetSoFListSuccess(dataList: MutableList<SoFUser>) {
        Timber.d("onGetSoFListSuccess")
        EventBus.getDefault().post(MessageEvent(EventMessage.LOAD_DATA_COMPLETE))

        sofUser.clear()
        sofUser.addAll(dataList)
        postState(ScreenState.Render(SoFListState.LoadUserDone))
        increasePage()
    }

    override fun onGetDataError(errorCode : Int, exception: Exception) {
        Timber.d("onGetSoFListError")
        postState(ScreenState.Render(SoFListState.LoadUserError))

    }

    override fun onReachedOutOfData() {
        Timber.d("onReachedOutOfData")
        postState(ScreenState.Render(SoFListState.ReachedOutOfData))
    }

    override fun onGetSoFFavoriteIdListSuccess(idList: List<String>) {
        Timber.d("onGetSoFFavoriteIdListSuccess")
        favoriteSofUserIdList = idList
        postState(ScreenState.Render(SoFListState.LoadFavoriteListDone))
    }

    override fun onGetSoFFavoriteListSuccess(dataList: MutableList<SoFUser>) {
        Timber.d("onGetSoFFavoriteListSuccess")
        sofUser.clear()
        sofUser.addAll(dataList)
        postState(ScreenState.Render(SoFListState.LoadUserDone))
    }
}

class SoFListViewModalFactory(private val sofInterator: SoFListIterator) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SoFListViewModal(sofInterator) as T
    }
}