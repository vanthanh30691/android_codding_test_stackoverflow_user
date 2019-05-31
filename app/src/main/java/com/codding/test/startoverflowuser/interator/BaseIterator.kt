package com.codding.test.startoverflowuser.interator

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

open class BaseIterator(application : Application) {

    // Coroutine components
    private val parentJob = Job()
    private val coroutineContext : CoroutineContext
        get() = parentJob + Dispatchers.IO

    protected val coroutineScope = CoroutineScope(coroutineContext)

}