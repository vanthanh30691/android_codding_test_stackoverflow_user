package com.codding.test.startoverflowuser.network

import java.lang.Exception

sealed class CustomResult<out T> {
    data class Success<T>(val data : T) : CustomResult<T>()
    data class Error(val errorCode: Int, val exception: Exception) : CustomResult<Nothing>()
}