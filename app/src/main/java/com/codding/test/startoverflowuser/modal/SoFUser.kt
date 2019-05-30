package com.codding.test.startoverflowuser.modal

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.codding.test.startoverflowuser.util.ApiConstant
import com.codding.test.startoverflowuser.util.RoomConstant
import com.google.gson.annotations.SerializedName

@Entity(tableName = RoomConstant.SOF_USER_TABLE_NAME)
class SoFUser {

    @PrimaryKey
    @ColumnInfo(name = ApiConstant.JSON_FIELD_USER_ID)
    @SerializedName(ApiConstant.JSON_FIELD_USER_ID)
    var userId = ""

    @ColumnInfo(name = ApiConstant.JSON_FIELD_USER_NAME)
    @SerializedName(ApiConstant.JSON_FIELD_USER_NAME)
    var userName = ""

    @ColumnInfo(name = ApiConstant.JSON_FIELD_PROFILE_IMAGE)
    @SerializedName(ApiConstant.JSON_FIELD_PROFILE_IMAGE)
    var profileImg = ""

    @ColumnInfo(name = ApiConstant.JSON_FIELD_LOCATION)
    @SerializedName(ApiConstant.JSON_FIELD_LOCATION)
    var location = ""

    @ColumnInfo(name = ApiConstant.JSON_FIELD_LAST_ACCESS_DATE)
    @SerializedName(ApiConstant.JSON_FIELD_LAST_ACCESS_DATE)
    var accessDate = ""

    @ColumnInfo(name = ApiConstant.JSON_FIELD_LAST_REPUTATION)
    @SerializedName(ApiConstant.JSON_FIELD_LAST_REPUTATION)
    var reputation = 0

}