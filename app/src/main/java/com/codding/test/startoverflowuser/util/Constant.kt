package com.codding.test.startoverflowuser.util

object Constant {
    val SOF_DATA_LOAD_PAGE_SIZE = 50
    val SOF_DATA_LOAD_PAGE_SIZE_ON_WIFI = 100
    // The number of items remain in list will trigger system load more in background
    // Only in WIFI mode
    val SOF_DATA_BACKGROUND_LOAD_PADDING = 40
}

enum class NetWorkConnectionState {
    NONE, WIFI, CELL
}