package com.codding.test.startoverflowuser.viewmodal

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codding.test.startoverflowuser.screenstate.ScreenState

open class BaseViewModal<T>(application: Application) : AndroidViewModel(application) {
    // Variables to controlled loaded data
    private var currentPage = 1

    // Screen state to communicate with Views
    val _modalState = MutableLiveData<ScreenState<T>>()
    val modalState : LiveData<ScreenState<T>>
        get() = _modalState


    protected fun postState(state : ScreenState<T>) {
        _modalState.postValue(state)
    }

    protected fun increasePage() {
        currentPage++
    }

    protected fun resetPage() {
        currentPage = 1
    }

    protected fun getCurrentPage() : Int {
        return currentPage
    }
}