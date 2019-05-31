package com.codding.test.startoverflowuser.listener

import java.lang.Exception

interface BaseIteratorListener {
    fun onReachedOutOfData()
    fun onGetDataError(errorCode : Int, exception: Exception)
}