package com.codding.test.startoverflowuser.viewmodal

import androidx.lifecycle.*
import com.codding.test.startoverflowuser.eventbus.MessageEvent
import com.codding.test.startoverflowuser.interator.SoFListIterator
import com.codding.test.startoverflowuser.modal.SoFUser
import com.codding.test.startoverflowuser.screenstate.ScreenState
import com.codding.test.startoverflowuser.screenstate.SoFListState
import com.codding.test.startoverflowuser.util.AppLogger
import com.codding.test.startoverflowuser.util.EventMessage
import org.greenrobot.eventbus.EventBus
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
        AppLogger.debug(this, "getSofUser")
        AppLogger.debug(this, pageSize)

        setState(ScreenState.Loading)
        sofInterator.loadSoFUser(getCurrentPage(), pageSize, this)
        lastLoadedPageSize = pageSize

    }

    fun refreshData(favoriteMode : Boolean) {
        resetPage()
        if (favoriteMode) getFavoriteUser()
        else getSofUser(lastLoadedPageSize)
    }

    fun toogleFavoriteState(sofUser : SoFUser) {
        AppLogger.debug(this, "toogleFavoriteState")
        AppLogger.debug(this, sofUser)

        sofInterator.toogleFavoriteState(sofUser,this)
    }

    fun getFavoriteUser() {
        AppLogger.debug(this, "getFavoriteUser")

        resetPage()
        setState(ScreenState.Loading)
        sofInterator.getSofFavoriteUsers(this)
    }

    /**
     * Interactor listeners
     */

    override fun onGetSoFListSuccess(dataList: MutableList<SoFUser>) {
        AppLogger.debug(this, "onGetSoFListSuccess")
        EventBus.getDefault().post(MessageEvent(EventMessage.LOAD_DATA_COMPLETE))

        sofUser.clear()
        sofUser.addAll(dataList)
        postState(ScreenState.Render(SoFListState.LoadUserDone))
        increasePage()
    }

    override fun onGetDataError(errorCode : Int, exception: Exception) {
        AppLogger.debug(this, "onGetSoFListError")
        postState(ScreenState.Render(SoFListState.LoadUserError))

    }

    override fun onReachedOutOfData() {
        AppLogger.debug(this, "onReachedOutOfData")
        postState(ScreenState.Render(SoFListState.ReachedOutOfData))
    }

    override fun onGetSoFFavoriteIdListSuccess(idList: List<String>) {
        AppLogger.debug(this, "onGetSoFFavoriteIdListSuccess")
        favoriteSofUserIdList = idList
        postState(ScreenState.Render(SoFListState.LoadFavoriteListDone))
    }

    override fun onGetSoFFavoriteListSuccess(dataList: MutableList<SoFUser>) {
        AppLogger.debug(this, "onGetSoFFavoriteListSuccess")
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