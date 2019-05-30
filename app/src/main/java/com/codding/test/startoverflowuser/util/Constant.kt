package com.codding.test.startoverflowuser.util

object Constant {
    const val SOF_DATA_LOAD_PAGE_SIZE = 50
    const val SOF_DATA_LOAD_PAGE_SIZE_ON_WIFI = 100
    // The number of items remain in list will trigger system load more in background
    // Only in WIFI mode
    const val SOF_DATA_BACKGROUND_LOAD_PADDING = 40
}

object RoomConstant {
    const val SOF_USER_TABLE_NAME = "SofUser"
}

object ApiConstant {
    const val JSON_FIELD_USER_ID = "user_id"
    const val JSON_FIELD_USER_NAME = "display_name"
    const val JSON_FIELD_LOCATION = "location"
    const val JSON_FIELD_PROFILE_IMAGE = "profile_image"
    const val JSON_FIELD_LAST_ACCESS_DATE = "last_access_date"
    const val JSON_FIELD_LAST_REPUTATION = "reputation"
}

enum class NetWorkConnectionState {
    NONE, WIFI, CELL
}