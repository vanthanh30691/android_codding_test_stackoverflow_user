package com.codding.test.startoverflowuser.viewmodal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codding.test.startoverflowuser.screenstate.ScreenState

open class BaseViewModal<T> : ViewModel() {
    // Variables to controlled loaded data
    private var currentPage = 1

    // Screen state to communicate with Views
    val _modalState = MutableLiveData<ScreenState<T>>()
    val modalState : LiveData<ScreenState<T>>
        get() = _modalState

    /**
     * Change view modal state using setValues
     */
    protected fun setState(state : ScreenState<T>) {
        _modalState.value = state
    }

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