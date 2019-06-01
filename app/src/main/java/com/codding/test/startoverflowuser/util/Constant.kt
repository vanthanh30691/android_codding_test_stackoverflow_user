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
    const val SOF_DATABASE_NAME = "SofDatabase"
    const val SOF_DATABASE_VERSION = 1
}

object ApiConstant {
    const val API_DEFAULT_SITE_PARAMETER = "stackoverflow"
    const val API_SITE_PARAMETER = "site"
    const val API_PAGE_PARAMETER = "page"
    const val API_PAGESIZE_PARAMETER = "pagesize"

    const val API_GET_SOF_USER = "users"
    const val API_USER_REPUTATIONS = "users/{user_id}/reputation-history"
    const val API_USER_REPUTATIONS_INPUT_PATH = "user_id"

    const val JSON_FIELD_USER_ID = "user_id"
    const val JSON_FIELD_USER_NAME = "display_name"
    const val JSON_FIELD_LOCATION = "location"
    const val JSON_FIELD_PROFILE_IMAGE = "profile_image"
    const val JSON_FIELD_LAST_ACCESS_DATE = "last_access_date"
    const val JSON_FIELD_LAST_REPUTATION = "reputation"

    const val JSON_FIELD_REPU_TYPE = "reputation_history_type"
    const val JSON_FIELD_REPU_CHANGE = "reputation_change"
    const val JSON_FIELD_POST_ID = "post_id"
    const val JSON_FIELD_CREATE_DATE = "creation_date"
}

object IntentCons {
    const val INTENT_USER_ID = "user_id"
    const val INTENT_REQUEST_LOADDING_SCREEN = 0x01
}

enum class NetWorkConnectionState {
    NONE, WIFI, CELL
}

object TimeConstant {
    const val LAST_ACCESS_TIME_FORMAT = "MMM dd yyyy"
}

object RepuType {
    const val REPU_TYPE_DOWN = "post_downvoted"
    const val REPU_TYPE_ACCEPT = "answer_accepted"
}

object EventMessage {
    const val LOAD_DATA_COMPLETE = 0x01
}


