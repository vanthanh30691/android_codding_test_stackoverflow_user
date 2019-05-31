package com.codding.test.startoverflowuser.modal

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.codding.test.startoverflowuser.util.ApiConstant
import com.codding.test.startoverflowuser.util.RoomConstant
import com.google.gson.annotations.SerializedName

class Reputation {

    @SerializedName(ApiConstant.JSON_FIELD_REPU_TYPE)
    var repuType = ""

    @SerializedName(ApiConstant.JSON_FIELD_REPU_CHANGE)
    var repuChange = ""

    @SerializedName(ApiConstant.JSON_FIELD_CREATE_DATE)
    var createAt = ""

    @SerializedName(ApiConstant.JSON_FIELD_POST_ID)
    var postId = ""


}